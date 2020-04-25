/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RegexValidator;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Roles;
import trip.be.Task;
import trip.be.TaskTime;
import trip.be.Timer;
import trip.gui.AppModel;
import trip.gui.models.ProjectModel;
import trip.gui.models.TaskModel;
import trip.utilities.StageOpener;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MainUserViewController implements Initializable {

    private ProjectModel projectModel = new ProjectModel();
    private TaskModel taskModel = new TaskModel();

    private Timer timer = new Timer();
    private Employee loggedUser = LoginController.loggedUser;

    @FXML
    private ComboBox<Project> projectComboBox;
    @FXML
    private TableView<Task> taskList;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> timeColumn;
    @FXML
    private TableView<TaskTime> taskTimerList;
    @FXML
    private TableColumn<TaskTime, String> durationColumn;
    @FXML
    private TableColumn<TaskTime, String> startColumn;
    @FXML
    private TableColumn<TaskTime, String> endColumn;
    @FXML
    private VBox menuBar;
    @FXML
    private TextField newTaskTitle;
    @FXML
    private JFXButton startTimer;
    @FXML
    private GridPane addTiimerPane;
    @FXML
    private Label timerLabel;
    @FXML
    private JFXButton stopTimer;
    @FXML
    private JFXButton cancelTimer;
    @FXML
    private ComboBox<Task> tasks;
    @FXML
    private JFXDatePicker dateStart;
    @FXML
    private JFXDatePicker dateEnd;
    @FXML
    private JFXTimePicker timeEnd;
    @FXML
    private JFXTimePicker timeStart;
    @FXML
    private JFXTextField timerField;
    @FXML
    private JFXButton addTime;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nameColumn.setCellValueFactory((data) -> {
            Task task = data.getValue();
            return new SimpleStringProperty(task.getName());
        });

        timeColumn.setCellValueFactory((data) -> {
            Task task = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertSecondsToString(task.getTotalTime()));
        });

        durationColumn.setCellValueFactory((data) -> {
            TaskTime taskTime = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertSecondsToString(taskTime.getTime()));
        });

        startColumn.setCellValueFactory((data) -> {
            TaskTime taskTime = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertDateToString(taskTime.getStartTime()));
        });

        endColumn.setCellValueFactory((data) -> {
            TaskTime taskTime = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertDateToString(taskTime.getStopTime()));
        });

        newTaskTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            decideTimerEnabled();
        });

        timeStart.set24HourView(true);
        timeEnd.set24HourView(true);

        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^([0-5][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$");
        regex.setMessage("Input is not a valid time");
        timerField.getValidators().add(regex);

        timerField.textProperty().addListener((Observable, oldValue, newValue) -> {
            decideAddTimeEnabled();
        });

        loadProjects();
        Platform.runLater(() -> {
            setupCloseRequest();
        });
    }

    public void setAdmin(Project project) {
        projectComboBox.getSelectionModel().select(project);
        taskList.setItems(taskModel.loadTasks(loggedUser.getId(), project.getId()));
        tasks.setItems(taskModel.loadTasks(loggedUser.getId(), project.getId()));
    }

    public void loadProjects() {

        if (loggedUser.getRole() == Roles.ADMIN) {
            projectComboBox.setItems(projectModel.loadAllActiveProjects(loggedUser.getId()));
        } else {
            projectComboBox.setItems(projectModel.loadAllUserProjects(loggedUser.getId()));
        }
    }

    @FXML
    private void switchProject(ActionEvent event) {
        taskList.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
        tasks.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
        decideTimerEnabled();
    }

    @FXML
    private void showTime(MouseEvent event) throws IOException {

        if (!taskList.getSelectionModel().isEmpty()) {
            taskTimerList.setItems(taskList.getSelectionModel().getSelectedItem().getTimeTasks());
            decideTimerEnabled();
        }

        if (event.getClickCount() > 1 & !taskList.getSelectionModel().isEmpty() & !event.isConsumed()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AppModel.class.getResource("views/UpdateTaskForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            UpdateTaskForm controller = fxmlLoader.getController();
            controller.setTask(updateView(), taskList.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }
    }

    private int addTask() {
        String taskName = newTaskTitle.getText().trim();
        int id = taskModel.addTask(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId(), taskName);
        updateView().start();
        return id;
    }

    @FXML
    private void startTimer(ActionEvent event) {

        if (!newTaskTitle.getText().trim().isEmpty()) {
            int id = addTask();
            newTaskTitle.clear();
            timer.startTimer(id, timerLabel);
        } else {
            timer.startTimer(taskList.getSelectionModel().getSelectedItem().getId(), timerLabel);
        }

        startTimer.setVisible(false);
        stopTimer.setVisible(true);
        cancelTimer.setVisible(true);
    }

    @FXML
    private void stopTimer(ActionEvent event) {
        timer.stopTimer();
        taskModel.saveTimeForTask(timer.getTaskId(), timer.getTime(), timer.getStartTime(), timer.getStopTime());
        startTimer.setVisible(true);
        stopTimer.setVisible(false);
        cancelTimer.setVisible(false);
        updateView().start();
    }

    @FXML
    private void cancelTime(ActionEvent event) {
        timer.stopTimer();
        startTimer.setVisible(true);
        stopTimer.setVisible(false);
        cancelTimer.setVisible(false);
    }

    @FXML
    private void addTime(ActionEvent event) {

        Task selectedTask = tasks.getSelectionModel().getSelectedItem();
        int taskId = (selectedTask != null) ? selectedTask.getId() : taskList.getSelectionModel().getSelectedItem().getId();

        int time = TimeConverter.convertStringToSeconds(timerField.getText());
        LocalDate localStart = dateStart.getValue();
        LocalDate localStop = dateEnd.getValue();

        LocalTime start = timeStart.getValue();
        LocalTime stop = timeEnd.getValue();

        Instant instantStart = localStart.atTime(start).atZone(ZoneId.systemDefault()).toInstant();
        Instant instantEnd = localStop.atTime(stop).atZone(ZoneId.systemDefault()).toInstant();

        Date startDate = Date.from(instantStart);
        Date endDate = Date.from(instantEnd);

        TaskTime taskTime = new TaskTime(time, startDate, endDate);

        taskModel.saveTimeForTask(taskId, time, startDate, endDate);

        timerField.setText("00:00:00");
        dateStart.setValue(null);
        dateEnd.setValue(null);
        timeStart.setValue(null);
        timeEnd.setValue(null);
        updateView().start();
    }

    private Thread updateView() {

        Thread updateThread = new Thread(() -> {

            Platform.runLater(() -> {
                if (!taskList.getSelectionModel().isEmpty()) {

                    Task selectedTask = taskList.getSelectionModel().getSelectedItem();
                    taskList.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
                    taskList.refresh();
                    taskList.getSelectionModel().select(selectedTask);
                    taskTimerList.getItems().clear();

                    if (taskList.getSelectionModel().getSelectedItem() != null) {

                        taskTimerList.setItems(taskList.getSelectionModel().getSelectedItem().getTimeTasks());
                    }
                    taskTimerList.refresh();
                } else {
                    taskList.setItems(taskModel.loadTasks(loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
                    taskList.refresh();
                }
                 Task selectedItem = tasks.getSelectionModel().getSelectedItem();
                tasks.setItems(taskList.getItems());
                tasks.getSelectionModel().select(selectedItem);
            });
        });
        return updateThread;
    }

    private void decideTimerEnabled() {
        if (!taskList.getSelectionModel().isEmpty()) {
            startTimer.setDisable(false);
        } else if (!newTaskTitle.getText().trim().isEmpty() && projectComboBox.getSelectionModel().getSelectedItem() != null) {
            startTimer.setDisable(false);
        } else {
            startTimer.setDisable(true);
        }
    }

    private void decideAddTimeEnabled() {
        if ((tasks.getSelectionModel().getSelectedItem() != null || !taskList.getSelectionModel().isEmpty()) && dateStart.getValue() != null && dateEnd.getValue() != null
                && timeStart.getValue() != null && timeEnd.getValue() != null && timerField.validate()) {
            addTime.setDisable(false);
        } else {
            addTime.setDisable(true);
        }
    }

    public void setupCloseRequest() {

        Stage appStage = (Stage) taskList.getScene().getWindow();

        appStage.setOnCloseRequest((e) -> {
            System.out.println("Closing thread");
            if (timer.isEnabled()) {
                timer.stopTimer();
                System.out.println("Closed");
            }
        });
    }

    @FXML
    private void openTaskTime(MouseEvent event) throws IOException {
        if (event.getClickCount() > 1 & !taskTimerList.getSelectionModel().isEmpty() & !event.isConsumed()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AppModel.class.getResource("views/UpdateTasktimeForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            UpdateTasktimeForm controller = fxmlLoader.getController();
            controller.setTaskTime(updateView(), taskTimerList.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void validateAddTask(ActionEvent event) {
        decideAddTimeEnabled();
    }

    @FXML
    private void open_time_view(MouseEvent event) {
    }

    @FXML
    private void log_out(MouseEvent event) {
        StageOpener.changeStage("views/Login.fxml", (Stage) taskList.getScene().getWindow());
    }

}
