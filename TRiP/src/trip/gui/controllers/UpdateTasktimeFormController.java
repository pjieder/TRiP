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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import trip.be.CountedTime;
import trip.gui.models.TaskModel;
import trip.utilities.JFXAlert;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author ander
 */
public class UpdateTasktimeFormController implements Initializable {

    private TaskModel taskModel = new TaskModel();

    private CountedTime countedTime;
    private Thread updateThread;

    @FXML
    private JFXButton updateButton;
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
            changeTime();
            decideUpdateTimeEnabled();
        });
    }

    /**
     * This method runs when the updateTasktimeForm is opened from the MainUserviewController. It takes the selected counted time and update thread and stores them as instance variables.
     *
     * @param thread the Thread returned by method updateView in the MainUserViewController.
     * @param countedTime the selected counted time to be updated.
     */
    public void setCountedTime(Thread thread, CountedTime countedTime) {
        this.countedTime = countedTime;
        this.updateThread = thread;
        dateStart.setValue(countedTime.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dateStop.setValue(countedTime.getStopTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        timeStart.setValue(countedTime.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        timeStop.setValue(countedTime.getStopTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

        timerField.setText(TimeConverter.convertSecondsToString(countedTime.getTime()));
    }

    /**
     * Updates the selected counted time with the newly entered information and closes the stage.
     *
     * @param event
     */
    @FXML
    private void updateCountedTime(ActionEvent event) {

        int time = TimeConverter.convertStringToSeconds(timerField.getText());
        LocalDate localStart = dateStart.getValue();
        LocalDate localStop = dateStop.getValue();

        LocalTime start = timeStart.getValue();
        LocalTime stop = timeStop.getValue();

        Instant instantStart = localStart.atTime(start).atZone(ZoneId.systemDefault()).toInstant();
        Instant instantEnd = localStop.atTime(stop).atZone(ZoneId.systemDefault()).toInstant();

        Date startDate = Date.from(instantStart);
        Date endDate = Date.from(instantEnd);

        countedTime.setTime(time);
        countedTime.setStartTime(startDate);
        countedTime.setStopTime(endDate);

        try {
            taskModel.updateTimeForTask(countedTime);
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error occured while trying to update task.");
        }
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
     * Deletes the selected countedTime. This will result in the registered time for the task being deleted as well.
     *
     * @param event
     */
    @FXML
    private void deleteCountedTime(ActionEvent event) {
        try {
            taskModel.deleteTimeForTask(countedTime);
            updateThread.start();
            closeStage();
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error occured while trying to delete time for the task.");
        }
    }

    /**
     * Validates the entered information and enables the updateButton is sufficient information is given.
     *
     * @param event
     */
    @FXML
    private void validateAddTask(ActionEvent event) {
        calculateTime();
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

    /**
     * Calculates the time between the selected date and time in order to display this
     * in the timerField for a precise logging time.
     */
    private void calculateTime() {
        if (dateStart.getValue() != null && dateStop.getValue() != null && timeStart.getValue() != null && timeStop.getValue() != null) {

            LocalDate localStart = dateStart.getValue();
            LocalDate localStop = dateStop.getValue();

            LocalTime start = timeStart.getValue();
            LocalTime stop = timeStop.getValue();

            Instant instantStart = localStart.atTime(start).atZone(ZoneId.systemDefault()).toInstant();
            Instant instantEnd = localStop.atTime(stop).atZone(ZoneId.systemDefault()).toInstant();

            Date startDate = Date.from(instantStart);
            Date endDate = Date.from(instantEnd);

            int seconds = (int) (endDate.getTime() - startDate.getTime()) / 1000;
            seconds = (seconds > 0) ? seconds : 0;

            timerField.clear();
            timerField.setText(TimeConverter.convertSecondsToString(seconds));
        }
    }

    /**
     * Calculates what the date and time fields should be to reflect the change
     * made to the timerField in order to display a precise logging time.
     */
    private void changeTime() {
        if (timerField.validate())
        {
        if (dateStart.getValue()== null){dateStart.setValue(LocalDate.now());}
        if (timeStart.getValue()==null){timeStart.setValue(LocalTime.now().withSecond(0));}
        
        int seconds = TimeConverter.convertStringToSeconds(timerField.getText());
        
        LocalDate localStart = dateStart.getValue();
        LocalTime start = timeStart.getValue();
        
        LocalDateTime dateTime = LocalDateTime.of(localStart, start);
        
        Instant instantStart = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        Instant instantEnd = dateTime.atZone(ZoneId.systemDefault()).toInstant().plusSeconds(seconds);
        
        dateStop.setValue(instantEnd.atZone(ZoneId.systemDefault()).toLocalDate());
        timeStop.setValue(instantEnd.atZone(ZoneId.systemDefault()).toLocalTime());
        }
    }

}
