/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Task;
import trip.be.TaskTime;
import trip.gui.AppModel;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MainUserViewController implements Initializable {

    private AppModel appModel = new AppModel();
    private Employee loggedEmployee;

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

    }

    public void setAdmin(Employee employee, Project project) {
        this.loggedEmployee = employee;
        projectComboBox.setItems(loggedEmployee.getProjects());
        projectComboBox.getSelectionModel().select(project);

        project.setTasks(loadTasks(loggedEmployee.getId(), project.getId()));
        taskList.setItems(project.getTasks());
    }

    public void setUser(Employee employee) {
        this.loggedEmployee = employee;
        projectComboBox.setItems(loggedEmployee.getProjects());
    }

    public ObservableList<Task> loadTasks(int userId, int projectId) {
        return appModel.loadTasks(loggedEmployee.getId(), projectId);
    }

    @FXML
    private void showTime(MouseEvent event) {

        if (!taskList.getSelectionModel().isEmpty()) {
            taskTimerList.setItems(taskList.getSelectionModel().getSelectedItem().getTimeTasks());
        }
    }

    @FXML
    private void switchProject(ActionEvent event) {
        taskList.setItems(loadTasks(loggedEmployee.getId(), projectComboBox.getSelectionModel().getSelectedItem().getId()));
    }

    
    
    
    
    
    
    
    
    
    
    
    
    @FXML
    private void open_statistics_view(MouseEvent event) {
    }

    @FXML
    private void open_time_view(MouseEvent event) {
    }

    @FXML
    private void log_out(MouseEvent event) {
    }

}
