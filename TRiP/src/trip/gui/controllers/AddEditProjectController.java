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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trip.be.Employee;
import trip.be.Project;
import trip.gui.models.ProjectModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AddEditProjectController implements Initializable
{
    
    private Thread updateThread;
    private Project projectToUpdate;
    
    private ProjectModel projectModel = new ProjectModel();
    
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField clientField;
    @FXML
    private JFXTextField rateField;
    @FXML
    private ComboBox<Employee> activeUsersCBox;
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
    public void initialize(URL url, ResourceBundle rb)
    {
        NumberValidator validator = new NumberValidator();
        validator.setMessage("Input is not a valid number");
        rateField.getValidators().add(validator);
        
        nameField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateInput();
        });
        
        clientField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateInput();
        });
        
        rateField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateInput();
            rateField.validate();
        });
    }
    
    private void validateInput()
    {
        if (nameField.getText().equals("") || clientField.getText().equals("") || rateField.getText().equals(""))
        {
            registerButton.setDisable(true);
            updateButton.setDisable(true);
        } else if (!rateField.validate())
        {
            registerButton.setDisable(true);
            updateButton.setDisable(true);
        } else
        {
            registerButton.setDisable(false);
            updateButton.setDisable(false);
        }
    }
    
    @FXML
    private void registerProject(ActionEvent event)
    {
        Project project = new Project();
        project.setName(nameField.getText());
        project.setRate(Double.parseDouble(rateField.getText().replace(",", ".")));

//        if(activeUsersList == null)
//        {
//            project.setIsActive(false);
//        }
//        else
//        {
        project.setIsActive(true);
//        }

        if (projectToUpdate == null)
        {
            projectModel.createProject(project);
        } else
        {
            project.setId(projectToUpdate.getId());
            projectModel.updateProject(project);
        }
        
        updateThread.start();
        closeScene();
    }
    
    @FXML
    private void addUserToProject(ActionEvent event)
    {
    }
    
    @FXML
    private void removeUser(MouseEvent event)
    {
    }
    
    @FXML
    private void cancelScene(ActionEvent event)
    {
        closeScene();
    }
    
    public void setUpdateThread(Thread thread)
    {
        this.updateThread = thread;
    }
    
    public void setProject(Thread thread, Project project)
    {
        this.updateThread = thread;
        this.projectToUpdate = project;
        
        registerButton.setVisible(false);
        updateButton.setVisible(true);
        
        nameField.setText(projectToUpdate.getName());
        clientField.setText("Client is not yet implemented bitch");
        rateField.setText(projectToUpdate.getRate() + "");
    }
    
    private void closeScene()
    {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
    }
}
