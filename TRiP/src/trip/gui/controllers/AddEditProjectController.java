/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trip.be.Employee;
import trip.be.Project;
import trip.gui.AppModel;
import trip.gui.models.ProjectModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AddEditProjectController implements Initializable {

    private Thread updateThread;
    private Project projectToUpdate;

    private ProjectModel projectModel = new ProjectModel();
    private AppModel appModel = new AppModel();

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField clientField;
    @FXML
    private JFXTextField rateField;
    @FXML
    private ChoiceBox<Employee> activeUsersCBox;
    @FXML
    private JFXListView<Employee> activeUsersList;
    @FXML
    private JFXButton registerButton;
    @FXML
    private Label removeUser;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton updateButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^[0-9]{1,10}([,.][0-9]{1,10})?$");
        regex.setMessage("Input is not a valid number");
        
        rateField.getValidators().add(regex);

        nameField.textProperty().addListener((Observable, oldValue, newValue)
                -> {
            validateInput();
        });

        clientField.textProperty().addListener((Observable, oldValue, newValue)
                -> {
            validateInput();
        });

        rateField.textProperty().addListener((Observable, oldValue, newValue)
                -> {
            validateInput();
            rateField.validate();
        });

        activeUsersCBox.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {
            addUserToProject();
        });

    }

    private void validateInput() {
        if (nameField.getText().equals("") || clientField.getText().equals("") || rateField.getText().equals("")) {
            registerButton.setDisable(true);
            updateButton.setDisable(true);
        } else if (!rateField.validate()) {
            registerButton.setDisable(true);
            updateButton.setDisable(true);
        } else {
            registerButton.setDisable(false);
            updateButton.setDisable(false);
        }
    }

    @FXML
    private void registerProject(ActionEvent event) {
        Project project = new Project();
        project.setName(nameField.getText());
        project.setRate(Double.parseDouble(rateField.getText().replace(",", ".")));

        if (activeUsersList.getItems().isEmpty()) {
            project.setIsActive(false);
        } else {
            project.setIsActive(true);
        }

        if (projectToUpdate == null) {
            projectModel.createProject(project, activeUsersList.getItems());
        } else {
            project.setId(projectToUpdate.getId());
            projectModel.updateProject(project, activeUsersList.getItems());
        }

        updateThread.start();
        closeScene();
    }

    private void addUserToProject() {

        if (activeUsersCBox.getValue() != null && !activeUsersList.getItems().contains(activeUsersCBox.getSelectionModel().getSelectedItem())) {

            Employee selectedItem = activeUsersCBox.getValue();
            activeUsersCBox.getItems().clear();
            activeUsersList.getItems().add(selectedItem);
            updateCheckBox();

        }
    }

    private void updateCheckBox() {
        ObservableList<Employee> updatedCombo = appModel.loadActiveUsers();

        for (Employee employee : activeUsersList.getItems()) {
            updatedCombo.remove(employee);
        }
        activeUsersCBox.setItems(updatedCombo);
    }

    @FXML
    private void removeUser(MouseEvent event) {
        if (!activeUsersList.getSelectionModel().isEmpty()) {
            activeUsersList.getItems().remove(activeUsersList.getSelectionModel().getSelectedItem());
            updateCheckBox();
        }
    }

    @FXML
    private void cancelScene(ActionEvent event) {
        closeScene();
    }

    public void setUpdateThread(Thread thread) {
        this.updateThread = thread;
        activeUsersCBox.setItems(appModel.loadActiveUsers());
    }

    public void setProject(Thread thread, Project project) {
        this.updateThread = thread;
        this.projectToUpdate = project;

        registerButton.setVisible(false);
        updateButton.setVisible(true);

        nameField.setText(projectToUpdate.getName());
        clientField.setText("Client is not yet implemented bitch");
        rateField.setText(projectToUpdate.getRate() + "");

        activeUsersList.setItems(appModel.loadEmployeesAssignedToProject(project.getId()));
        updateCheckBox();

    }

    private void closeScene() {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
    }

}
