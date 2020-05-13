/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXProgressBar;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Task;
import trip.gui.TRiP;
import trip.gui.models.EmployeeModel;
import trip.gui.models.ProjectModel;
import trip.gui.models.TaskModel;
import trip.utilities.JFXAlert;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AdminStatisticsViewController implements Initializable {

    private ProjectModel projectModel = new ProjectModel();
    private EmployeeModel appModel = new EmployeeModel();
    private TaskModel taskModel = new TaskModel();
    private DecimalFormat df = new DecimalFormat("0.0#");

    @FXML
    private LineChart<String, Double> lineChart;
    @FXML
    private BarChart<String, Double> barChart;
    @FXML
    private ComboBox<String> statisticComboBox;
    @FXML
    private JFXDatePicker dateEnd;
    @FXML
    private JFXDatePicker dateStart;
    @FXML
    private ComboBox<Project> projectComboBox;
    @FXML
    private JFXProgressBar progress;
    @FXML
    private ComboBox<Employee> employeeComboBox;
    @FXML
    private JFXButton calculateLine;
    @FXML
    private JFXButton calculateBar;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private JFXButton openEmployeeBtn;
    @FXML
    private ComboBox<Employee> employeeSelection;
    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> taskColumn;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> hourColumn;
    @FXML
    private TableColumn<Task, String> priceColumn;
    @FXML
    private JFXButton calculateTask;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        taskColumn.setCellValueFactory((data) -> {

            Task task = data.getValue();
            return new SimpleStringProperty(task.getName());
        });

        nameColumn.setCellValueFactory((data) -> {

            Task task = data.getValue();
            return new SimpleStringProperty(task.getEmployee());
        });

        hourColumn.setCellValueFactory((data) -> {

            Task task = data.getValue();
            double hours = (double) task.getTotalTime() / 3600;

            return new SimpleStringProperty(df.format(hours));
        });

        priceColumn.setCellValueFactory((data) -> {

            Task task = data.getValue();
            double price = ((double) task.getTotalTime() / 3600) * projectComboBox.getValue().getRate();

            return new SimpleStringProperty(df.format(price));
        });

        try {
            projectComboBox.setItems(projectModel.loadAllActiveProjects());
            projectComboBox.getItems().add(0, new Project("All projects", 0));
            projectComboBox.getSelectionModel().select(0);

            statisticComboBox.getItems().add("Project chart");
            statisticComboBox.getItems().add("Employee chart");
            statisticComboBox.getItems().add("Task Chart");
            statisticComboBox.getSelectionModel().select(0);

            employeeComboBox.setItems(appModel.loadActiveEmployees());
            employeeSelection.setItems(appModel.loadActiveEmployees());
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error intitializing.");
        }

    }

    /**
     * Changes the statistics shown based on what is selected in the statistics combo box.
     *
     * @param event
     */
    @FXML
    private void changeStatistic(ActionEvent event) {
        if (statisticComboBox.getSelectionModel().getSelectedIndex() == 0) {
            employeeComboBox.setVisible(false);
            projectComboBox.setVisible(true);
            lineChart.setVisible(true);
            barChart.setVisible(false);
            calculateLine.setVisible(true);
            calculateBar.setVisible(false);
            calculateTask.setVisible(false);
            taskTable.setVisible(false);
            totalTimeLabel.setText("00:00:00");
            totalPriceLabel.setText("0.00 DKK");
        } else if (statisticComboBox.getSelectionModel().getSelectedIndex() == 1) {
            employeeComboBox.setVisible(true);
            projectComboBox.setVisible(false);
            lineChart.setVisible(false);
            barChart.setVisible(true);
            calculateLine.setVisible(false);
            calculateBar.setVisible(true);
            calculateTask.setVisible(false);
            taskTable.setVisible(false);
            totalTimeLabel.setText("00:00:00");
            totalPriceLabel.setText("0.00 DKK");
        } else if (statisticComboBox.getSelectionModel().getSelectedIndex() == 2) {
            employeeComboBox.setVisible(false);
            projectComboBox.setVisible(true);
            lineChart.setVisible(false);
            barChart.setVisible(false);
            calculateLine.setVisible(false);
            calculateBar.setVisible(false);
            calculateTask.setVisible(true);
            taskTable.setVisible(true);
            totalTimeLabel.setText("00:00:00");
            totalPriceLabel.setText("0.00 DKK");
        }
    }

    /**
     * Validates whether or not the calculateBar- or calculateLine button is enabled or disabled when a change happens.
     *
     * @param event
     */
    @FXML
    private void validate(ActionEvent event) {
        validate();
    }

    private void validate() {

        if (dateStart.getValue() != null && dateEnd.getValue() != null) {
            calculateLine.setDisable(false);
            if (projectComboBox.getValue().getId() > 0) {
                calculateTask.setDisable(false);
            }

            if (employeeComboBox.getValue() != null) {
                calculateBar.setDisable(false);
            }
        } else {
            calculateLine.setDisable(true);
            calculateBar.setDisable(true);
            calculateBar.setDisable(true);
        }
    }

    /**
     * Calculates the statistics shown in the linechart based on the information entered.
     *
     * @param event
     */
    @FXML
    private void calculateLine(ActionEvent event) {
        progress.setVisible(true);
        calculateLine.setDisable(true);

        Thread thread = new Thread(() -> {
            try {
                LocalDate localDateFirst = dateStart.getValue();
                LocalDate localDateLast = dateEnd.getValue();
                Project selectedProject = projectComboBox.getValue();
                XYChart.Series series = projectModel.calculateGraphLine(selectedProject.getId(), localDateFirst, localDateLast);
                calculatePriceForLine(localDateFirst, localDateLast, selectedProject.getId());
                Platform.runLater(() -> {
                    lineChart.getData().clear();
                    lineChart.getData().add(series);
                    progress.setVisible(false);
                    validate();
                });
            } catch (SQLException ex) {
                Platform.runLater(() -> {
                    JFXAlert.openError(stackPane, "Error Calculating Statistics.");
                });
            }
        });
        thread.start();
    }

    /**
     * Calculates the statistics shown in the barchart based on the information entered.
     *
     * @param event
     */
    @FXML
    private void calculateBar(ActionEvent event) {
        progress.setVisible(true);
        calculateBar.setDisable(true);

        Thread thread = new Thread(() -> {
            try {
                LocalDate localDateFirst = dateStart.getValue();
                LocalDate localDateLast = dateEnd.getValue();
                Employee selectedEmployee = employeeComboBox.getValue();
                XYChart.Series series = projectModel.calculateGraphBar(localDateFirst, localDateLast, selectedEmployee.getId());
                calculatePriceForBar(localDateFirst, localDateLast, selectedEmployee.getId());
                Platform.runLater(() -> {
                    barChart.getData().clear();
                    barChart.getData().add(series);
                    progress.setVisible(false);
                    validate();
                });
            } catch (SQLException ex) {
                Platform.runLater(() -> {
                    JFXAlert.openError(stackPane, "Error Calculating Statistics.");
                });
            }
        });
        thread.start();
    }

    /**
     * Calculates the total hours worked in the period and the total amount of money earned based on the rate of the different projects.
     *
     * @param startDate The startdate of the period the statistics should be calculated upon.
     * @param endDate The enddate of the period the statistics should be calculated upon.
     * @param projectID The ID of the selected project.
     */
    private void calculatePriceForLine(LocalDate startDate, LocalDate endDate, int projectID) {
        int totalTime = 0;
        double totalPrice = 0;
        String totalTimeString;
        String totalPriceString;

        try {
            if (projectID == 0) {

                for (Project project : projectModel.loadAllActiveProjects()) {

                    double timeForProject = projectModel.loadAllBillableProjectTimeBetweenDates(project.getId(), startDate, endDate);
                    totalPrice += (timeForProject / 3600) * project.getRate();
                    totalTime += timeForProject;
                }
            } else {
                totalTime = projectModel.loadAllBillableProjectTimeBetweenDates(projectID, startDate, endDate);
                totalPrice = ((double) totalTime / 3600) * projectComboBox.getValue().getRate();
            }
            totalTimeString = TimeConverter.convertSecondsToString(totalTime);
            totalPriceString = df.format(totalPrice) + " DKK";

            Platform.runLater(() -> {
                totalTimeLabel.setText(totalTimeString);
                totalPriceLabel.setText(totalPriceString);
            });
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                JFXAlert.openError(stackPane, "Error Calculating Price.");
            });
        }
    }

    /**
     * Calculates the total hours worked in the period and the total amount of money earned based on the rate of the different projects.
     *
     * @param startDate The startdate of the period the statistics should be calculated upon.
     * @param endDate The enddate of the period the statistics should be calculated upon.
     * @param employeeeID The ID of the selected employee.
     */
    private void calculatePriceForBar(LocalDate startDate, LocalDate endDate, int employeeID) {

        int totalTime = 0;
        double totalPrice = 0;
        String totalTimeString;
        String totalPriceString;

        try {
            List<Project> allWorkedOnProjects = projectModel.loadWorkedOnProjectsBetweenDates(startDate, endDate, employeeID);

            for (Project project : allWorkedOnProjects) {

                double timeForProject = projectModel.loadAllBillableEmployeeProjectTimeBetweenDates(employeeID, project.getId(), startDate, endDate);
                totalPrice += (timeForProject / 3600) * project.getRate();
                totalTime += timeForProject;
            }

            totalTimeString = TimeConverter.convertSecondsToString(totalTime);
            totalPriceString = df.format(totalPrice) + " DKK";

            Platform.runLater(() -> {
                totalTimeLabel.setText(totalTimeString);
                totalPriceLabel.setText(totalPriceString);
            });
        } catch (SQLException ex) {
            Platform.runLater(() -> {
                JFXAlert.openError(stackPane, "Error Calculating Price.");
            });
        }
    }

    /**
     * Opens the time tracking of the selected employee. This enables the admin looking at what the users has been working on and the logged time.
     *
     * @param event
     */
    @FXML
    private void openEmployee(ActionEvent event) {
        if (employeeSelection.getValue() != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class
                        .getResource("views/MainUserView.fxml"));

                Pane pane = fxmlLoader.load();
                MainUserViewController controller = fxmlLoader.getController();
                controller.setEmployee(employeeSelection.getSelectionModel().getSelectedItem());

                MenuBarViewController.viewPane.getChildren().clear();
                MenuBarViewController.viewPane.getChildren().add(pane);
            } catch (IOException ex) {
                JFXAlert.openError(stackPane, "Error loading main user view.");
            }
        }
    }

    @FXML
    private void calculateTask(ActionEvent event) {

        try {
            taskTable.setItems(taskModel.loadAllUniqueTasksDates(projectComboBox.getValue().getId(), dateStart.getValue(), dateEnd.getValue()));
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error loading tasks.");
        }

    }

}
