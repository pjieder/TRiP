/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import trip.be.Task;
import trip.gui.models.TaskModel;
import trip.utilities.JFXAlert;

/**
 * FXML Controller class
 *
 * @author ander
 */
public class UpdateTaskForm implements Initializable {

    private TaskModel taskModel = new TaskModel();

    private Task task;
    private Thread updateThread;

    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXTextField taskNameField;
    @FXML
    private StackPane stackPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Task name required");

        taskNameField.getValidators().add(validator);
        taskNameField.textProperty().addListener((Observable, oldValue, newValue) -> {
            updateButton.setDisable(!taskNameField.validate());
        });
    }

    /**
     * This method runs when the updateTaskForm FXML is opened from the MainUserViewController. It takes the selected task and update thread and stores them as instance variables.
     * @param updateThread the Thread returned by method updateView in the MainUserViewController.
     * @param task the selected task to be updated.
     */
    public void setTask(Thread updateThread, Task task)
    {
        this.updateThread = updateThread;
        this.task = task;
        taskNameField.setText(task.getName());
    }
    
    /**
     * Updates the selected task with the newly entered information and closes the stage.
     * @param event 
     */
    @FXML
    private void updateTask(ActionEvent event) {
        try{
        task.setName(taskNameField.getText());
        taskModel.updateTask(task);
        updateThread.start();
        closeStage();
        }catch(SQLException ex){JFXAlert.openError(stackPane, "Error updating task.");}
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
     * Deletes the selected task. This will result in all registered time for the task being deleted as well. A warning will pop up, warning the user if they really
     * want to delete the selected task.
     * @param event 
     */
    @FXML
    private void deleteTask(ActionEvent event) {

        try{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirm delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this task? This will remove all associated time for the task.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            taskModel.deleteTask(task);
            updateThread.start();
            closeStage();
        } else {
            alert.close();
        }
        }catch(SQLException ex){JFXAlert.openError(stackPane, "Error occured while trying to delete task.");}
    }

    /**
     * Closes the stage without saving any information.
     */
    private void closeStage() {
        Stage currentStage = (Stage) taskNameField.getScene().getWindow();
        currentStage.close();
    }

}
