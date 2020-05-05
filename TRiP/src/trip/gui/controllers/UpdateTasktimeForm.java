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
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import trip.be.TaskTime;
import trip.gui.models.TaskModel;
import trip.utilities.JFXAlert;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author ander
 */
public class UpdateTasktimeForm implements Initializable {

    private TaskModel taskModel = new TaskModel();

    private TaskTime taskTime;
    private Thread updateThread;

    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXDatePicker dateStart;
    @FXML
    private JFXDatePicker dateStop;
    @FXML
    private JFXTimePicker timeStart;
    @FXML
    private JFXTextField timerField;
    @FXML
    private JFXTimePicker timeStop;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private StackPane stackPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeStart.set24HourView(true);
        timeStop.set24HourView(true);

        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^([0-5][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$");
        regex.setMessage("Input is not a valid time");
        timerField.getValidators().add(regex);

        timerField.textProperty().addListener((Observable, oldValue, newValue) -> {
            decideUpdateTimeEnabled();
        });
    }

    /**
     * This method runs when the updateTasktimeForm is opened from the MainUserviewController. It takes the selected taskTime and update thread and stores them as instance variables.
     *
     * @param thread the Thread returned by method updateView in the MainUserViewController.
     * @param taskTime the selected taskTime to be updated.
     */
    public void setTaskTime(Thread thread, TaskTime taskTime) {
        this.taskTime = taskTime;
        this.updateThread = thread;
        dateStart.setValue(taskTime.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dateStop.setValue(taskTime.getStopTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        timeStart.setValue(taskTime.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        timeStop.setValue(taskTime.getStopTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

        timerField.setText(TimeConverter.convertSecondsToString(taskTime.getTime()));
    }

    /**
     * Updates the selected taskTime with the newly entered information and closes the stage.
     *
     * @param event
     */
    @FXML
    private void updateTasktime(ActionEvent event) {

        int time = TimeConverter.convertStringToSeconds(timerField.getText());
        LocalDate localStart = dateStart.getValue();
        LocalDate localStop = dateStop.getValue();

        LocalTime start = timeStart.getValue();
        LocalTime stop = timeStop.getValue();

        Instant instantStart = localStart.atTime(start).atZone(ZoneId.systemDefault()).toInstant();
        Instant instantEnd = localStop.atTime(stop).atZone(ZoneId.systemDefault()).toInstant();

        Date startDate = Date.from(instantStart);
        Date endDate = Date.from(instantEnd);

        taskTime.setTime(time);
        taskTime.setStartTime(startDate);
        taskTime.setStopTime(endDate);

        try{
        taskModel.UpdateTimeForTask(taskTime);
        }catch(SQLException ex){JFXAlert.openError(stackPane, "Error occured while trying to update task.");}
        updateThread.start();
        closeStage();
    }

    /**
     * Cancels all actions and closes the stage.
     *
     * @param event
     */
    @FXML
    private void cancelScene(ActionEvent event) {
        closeStage();
    }

    /**
     * Deletes the selected taskTime. This will result in the registered time for the task being deleted as well.
     *
     * @param event
     */
    @FXML
    private void deleteTasktime(ActionEvent event) {
        try{
        taskModel.DeleteTimeForTask(taskTime);
        updateThread.start();
        closeStage();
        }catch(SQLException ex){JFXAlert.openError(stackPane, "Error occured while trying to delete time for the task.");}
    }

    /**
     * Validates the entered information and enables the updateButton is sufficient information is given.
     *
     * @param event
     */
    @FXML
    private void validateAddTask(ActionEvent event) {
        decideUpdateTimeEnabled();
    }

    /**
     * Closes the stage without saving any information.
     */
    private void closeStage() {
        Stage currentStage = (Stage) timeStart.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Validates the entered information and enables the updateButton is sufficient information is given.
     */
    private void decideUpdateTimeEnabled() {
        if (dateStart.getValue() != null && dateStop.getValue() != null && timeStart.getValue() != null
                && timeStop.getValue() != null && timerField.validate()) {
            updateButton.setDisable(false);
        } else {
            updateButton.setDisable(true);
        }
    }

}
