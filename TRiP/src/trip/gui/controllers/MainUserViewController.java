/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private AppModel appModel = new AppModel();
    private ProjectModel projectModel = new ProjectModel();
    private TaskModel taskModel = new TaskModel();

    private Timer timer = new Timer();

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

        loadProjects();
        Platform.runLater(() -> {
            setupCloseRequest();
        });
    }

    public void loadProjects() {

        Employee loggedUser = LoginController.loggedUser;

        if (loggedUser.getRole() == Roles.ADMIN) {
            projectComboBox.setItems(projectModel.loadAllActiveProjects(loggedUser.getId()));
        } else {
            projectComboBox.setItems(projectModel.loadAllUserProjects(loggedUser.getId()));
        }
    }

    public void setAdmin(Project project) {
        projectComboBox.getSelectionModel().select(project);
        taskList.setItems(taskModel.loadTasks(LoginController.loggedUser.getId(), project.getId()));
    }

    @FXML
    private void showTime(MouseEvent event) {

        if (!taskList.getSelectionModel().isEmpty()) {
            taskTimerList.setItems(taskList.getSelectionModel().getSelectedItem().getTimeTasks());
            decideTimerEnabled();
        }
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

    @FXML
    private void switchProject(ActionEvent event) {
        taskList.setItems(taskModel.loadTasks(LoginController.loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
        decideTimerEnabled();
    }

    @FXML
    private void open_time_view(MouseEvent event) {
    }

    @FXML
    private void log_out(MouseEvent event) {
        StageOpener.changeStage("views/Login.fxml", (Stage) taskList.getScene().getWindow());
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
        taskModel.saveTimeForTask(timer);
        updateView();
        startTimer.setVisible(true);
        stopTimer.setVisible(false);
        cancelTimer.setVisible(false);
    }
    
    
    @FXML
    private void cancelTime(ActionEvent event) {
        timer.stopTimer();
        startTimer.setVisible(true);
        stopTimer.setVisible(false);
        cancelTimer.setVisible(false);
    }

    private void updateView() {
        if (!taskList.getSelectionModel().isEmpty()) {
            int location = taskList.getSelectionModel().getSelectedIndex();
            taskList.setItems(taskModel.loadTasks(LoginController.loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
            taskList.refresh();
            taskList.getSelectionModel().select(location);
            taskTimerList.setItems(taskList.getSelectionModel().getSelectedItem().getTimeTasks());
        } else {
            taskList.setItems(taskModel.loadTasks(LoginController.loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
            taskList.refresh();
        }

    }

    private int addTask() {
        int id;

        String taskName = newTaskTitle.getText().trim();
        Task taskToAdd = new Task(taskName);
        id = taskModel.addTask(LoginController.loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId(), taskName);
        taskList.setItems(taskModel.loadTasks(LoginController.loggedUser.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));

        return id;
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


}
