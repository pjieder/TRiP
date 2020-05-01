/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import trip.be.Employee;
import trip.be.Project;
import trip.gui.AppModel;
import trip.gui.models.ProjectModel;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AdminStatisticsViewController implements Initializable {

    private ProjectModel projectModel = new ProjectModel();
    private AppModel appModel = new AppModel();

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        projectComboBox.setItems(projectModel.loadAllActiveProjects());
        projectComboBox.getItems().add(0, new Project("All projects", 0));
        projectComboBox.getSelectionModel().select(0);

        statisticComboBox.getItems().add("Project chart");
        statisticComboBox.getItems().add("Employee chart");
        statisticComboBox.getSelectionModel().select(0);

        employeeComboBox.setItems(appModel.loadActiveEmployees());

    }

    @FXML
    private void changeStatistic(ActionEvent event) {
        if (statisticComboBox.getSelectionModel().getSelectedIndex() == 0) {
            employeeComboBox.setVisible(false);
            projectComboBox.setVisible(true);
            lineChart.setVisible(true);
            barChart.setVisible(false);
            calculateLine.setVisible(true);
            calculateBar.setVisible(false);
            totalTimeLabel.setText("00:00:00");
            totalPriceLabel.setText("0.00 DKK");
        } else if (statisticComboBox.getSelectionModel().getSelectedIndex() == 1) {
            employeeComboBox.setVisible(true);
            projectComboBox.setVisible(false);
            lineChart.setVisible(false);
            barChart.setVisible(true);
            calculateLine.setVisible(false);
            calculateBar.setVisible(true);
            totalTimeLabel.setText("00:00:00");
            totalPriceLabel.setText("0.00 DKK");
        }
    }

    @FXML
    private void validate(ActionEvent event) {
        validate();
    }

    private void validate() {

        if (dateStart.getValue() != null && dateEnd.getValue() != null) {
            calculateLine.setDisable(false);

            if (employeeComboBox.getValue() != null) {
                calculateBar.setDisable(false);
            }
        } else {
            calculateLine.setDisable(true);
            calculateBar.setDisable(true);
        }
    }

    @FXML
    private void calculateLine(ActionEvent event) {
        progress.setVisible(true);
        calculateLine.setDisable(true);

        Thread thread = new Thread(() -> {

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
        });

        thread.start();
    }

    @FXML
    private void calculateBar(ActionEvent event) {
        progress.setVisible(true);
        calculateBar.setDisable(true);

        Thread thread = new Thread(() -> {

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
        });

        thread.start();
    }

    private void calculatePriceForLine(LocalDate startDate, LocalDate endDate, int projectID) {
        int totalTime = 0;
        double totalPrice = 0;
        String totalTimeString;
        String totalPriceString;

        DecimalFormat df = new DecimalFormat("0.0#");

        if (projectID == 0) {

            for (Project project : projectModel.loadAllActiveProjects()) {

                double timeForProject = projectModel.loadAllProjectTimeBetweenDates(project.getId(), startDate, endDate);
                totalPrice += (timeForProject / 3600) * project.getRate();
                totalTime += timeForProject;
            }

        } else {

            totalTime = projectModel.loadAllProjectTimeBetweenDates(projectID, startDate, endDate);
            totalPrice = ((double) totalTime / 3600) * projectComboBox.getValue().getRate();
        }
        totalTimeString = TimeConverter.convertSecondsToString(totalTime);
        totalPriceString = df.format(totalPrice) + " DKK";

        Platform.runLater(() -> {
            totalTimeLabel.setText(totalTimeString);
            totalPriceLabel.setText(totalPriceString);
        });
    }

    private void calculatePriceForBar(LocalDate startDate, LocalDate endDate, int employeeID) {

        int totalTime = 0;
        double totalPrice = 0;
        String totalTimeString;
        String totalPriceString;

        DecimalFormat df = new DecimalFormat("0.0#");
        List<Project> allWorkedOnProjects = projectModel.loadWorkedOnProjectsBetweenDates(startDate, endDate, employeeID);

        for (Project project : allWorkedOnProjects) {

            double timeForProject = projectModel.loadAllEmployeeProjectTimeBetweenDates(employeeID, project.getId(), startDate, endDate);
            totalPrice += (timeForProject / 3600) * project.getRate();
            totalTime += timeForProject;
        }

        totalTimeString = TimeConverter.convertSecondsToString(totalTime);
        totalPriceString = df.format(totalPrice) + " DKK";

        Platform.runLater(() -> {
            totalTimeLabel.setText(totalTimeString);
            totalPriceLabel.setText(totalPriceString);
        });

    }

}
