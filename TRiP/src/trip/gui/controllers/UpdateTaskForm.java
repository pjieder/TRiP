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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import trip.be.Task;
import trip.gui.models.TaskModel;

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

    public void setTask(Thread updateThread, Task task)
    {
        this.updateThread = updateThread;
        this.task = task;
        taskNameField.setText(task.getName());
    }
    
    @FXML
    private void updateTask(ActionEvent event) {
        task.setName(taskNameField.getText());
        taskModel.updateTask(task);
        updateThread.start();
        closeStage();
    }

    @FXML
    private void cancelScene(ActionEvent event) {
        closeStage();
    }

    @FXML
    private void deleteTask(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirm delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this task? This will remove all associated time for the task.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            taskModel.deleteTask(task.getId());
            updateThread.start();
            closeStage();
        } else {
            alert.close();
        }
    }

    private void closeStage() {
        Stage currentStage = (Stage) taskNameField.getScene().getWindow();
        currentStage.close();
    }

}
