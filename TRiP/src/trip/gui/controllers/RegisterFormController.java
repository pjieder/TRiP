/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import trip.be.Admin;
import trip.be.Employee;
import trip.be.Roles;
import trip.be.User;
import trip.gui.models.EmployeeModel;
import trip.utilities.JFXAlert;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class RegisterFormController implements Initializable {
    
    private EmployeeModel employeeModel = new EmployeeModel();
    private Thread updateThread;
    private Employee employeeToUpdate;
    
    @FXML
    private JFXCheckBox adminCheckbox;
    @FXML
    private JFXCheckBox userCheckbox;
    @FXML
    private JFXButton registerButton;
    @FXML
    private JFXTextField firstNameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private Label passwordError;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXPasswordField passwordFieldTwo;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXCheckBox passwordVisibility;
    @FXML
    private StackPane stackPane;
    @FXML
    private StackPane stackPaneExistingUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^.+@[^\\.].*\\.[a-z]{2,}$");
        regex.setMessage("Input is not a valid email");
        
        firstNameField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateInput();
        });
        
        lastNameField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateInput();
        });
        
        emailField.getValidators().add(regex);
        emailField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            emailField.validate();
            validateInput();
        });
        
        passwordField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateIdenticalPassword();
            validateInput();
        });
        
        passwordFieldTwo.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateIdenticalPassword();
            validateInput();
        });
        
    }
    
    /**
     * Validates the entered information and enables the register- or update button if sufficient information is given.
     */
    private void validateInput() {
        
        String fName = firstNameField.getText().trim();
        String lName = lastNameField.getText().trim();
        String email = emailField.getText();
        String password = passwordField.getText();
        String password2 = passwordFieldTwo.getText();
        
        if (passwordVisibility.isSelected() || !passwordVisibility.isVisible())
        {
            if (fName.equals("") || lName.equals("") || email.equals("") || password.equals("") || password2.equals(""))
            {
                registerButton.setDisable(true);
                updateButton.setDisable(true);
            } else if (!emailField.validate() || !validateIdenticalPassword())
            {
                registerButton.setDisable(true);
                updateButton.setDisable(true);
            } else
            {
                registerButton.setDisable(false);
                updateButton.setDisable(false);
            }
        } else
        {
            if (fName.equals("") || lName.equals("") || email.equals(""))
            {
                updateButton.setDisable(true);
            } else if (!emailField.validate())
            {
                updateButton.setDisable(true);
            } else
            {
                updateButton.setDisable(false);
            }
        }
    }
    
    /**
     * Validates whether or not the passwords entered are identical.
     * @return boolean representing whether or not the intered passwords are identical.
     */
    private boolean validateIdenticalPassword() {
        if (!passwordField.getText().equals(passwordFieldTwo.getText()))
        {
            passwordError.setVisible(true);
            return false;
        } else
        {
            passwordError.setVisible(false);
            return true;
        }
    }
    
    /**
     * This methods runs when the RegisterForm FXML is opened by the "add" button. It takes the update statistics thread 
     * and stores it as an instance variable.
     *
     * @param thread the Thread returned by method getUpdateListThread in the AdminCurrentUserViewController
     */
    public void setUpdateThread(Thread thread) {
        this.updateThread = thread;
    }
    
    /**
     * This method runs when the RegisterForm FXML is opened by the "update" button. It takes the selected employee 
     * and update thread and stores them as instance variables.
     * @param employee The employee to be updated
     * @param thread the Thread returned by method getUpdateListThread in the AdminCurrentUserViewController.
     */
    public void setEmployee(Employee employee, Thread thread) {
        this.updateThread = thread;
        this.employeeToUpdate = employee;
        
        registerButton.setVisible(false);
        updateButton.setVisible(true);
        passwordVisibility.setVisible(true);
        passwordField.setVisible(false);
        passwordFieldTwo.setVisible(false);
        
        firstNameField.setText(employee.getfName());
        lastNameField.setText(employee.getlName());
        emailField.setText(employee.getEmail());
        adminCheckbox.setSelected(employee.getRole() == Roles.ADMIN);
        userCheckbox.setSelected(employee.getRole() == Roles.USER);
        
        validateInput();
    }
    
    /**
     * Saves or updates the employee based on what button was pressed when opening the FXML.
     *
     * @param event
     */
    @FXML
    private void registerEmployee(ActionEvent event) {
        Employee newEmployee;
        
        String fName = firstNameField.getText().trim();
        String lName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        try{
        if (adminCheckbox.isSelected())
        {
            newEmployee = new Admin(fName, lName, email);
            
        } else
        {
            newEmployee = new User(fName, lName, email);
        }
        
        if (employeeToUpdate == null)
        {
            if (!employeeModel.createEmployee(newEmployee, password)){JFXAlert.openError(stackPaneExistingUser, "User with that email already exists. Please use a different one."); return;}
        } else
        {
            newEmployee.setId(employeeToUpdate.getId());
            employeeModel.updateEmployee(newEmployee);
            
            if (passwordVisibility.isSelected())
            {
                employeeModel.updatePassword(password, newEmployee);
            }
        }
        updateThread.start();
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        currentStage.close();
        }catch(SQLException ex){JFXAlert.openError(stackPane, "Error registering or updating employee.");}
    }

    /**
     * Cancels all actions and closes the stage.
     *
     * @param event
     */
    @FXML
    private void cancelScene(ActionEvent event) {
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        currentStage.close();
    }
    
    /**
     * Enables or disables the two password fields based on whether or not the admin wants to update the passwords
     * when making changes to the selected user.
     * @param event 
     */
    @FXML
    private void makePasswordVisible(ActionEvent event) {
        
        if (passwordField.isVisible() == false)
        {
            passwordField.setVisible(true);
            passwordFieldTwo.setVisible(true);
        } else
        {
            passwordField.setVisible(false);
            passwordFieldTwo.setVisible(false);
        }
        validateInput();
    }
    
    /**
     * Selectes the userCheckBox based on whether or not the adminCheckBox is selected.
     * @param event 
     */
    @FXML
    private void doAdmin(ActionEvent event) {
        
        if (adminCheckbox.isSelected())
        {userCheckbox.setSelected(false);} 
        else
        {userCheckbox.setSelected(true);}
        
    }
    
    /**
     * Selects the adminCheckBox based on whether or not the userCheckBox is selected.
     * @param event 
     */
    @FXML
    private void doUser(ActionEvent event) {
        
        if (userCheckbox.isSelected())
        {
            adminCheckbox.setSelected(false);
        } else
        {
            adminCheckbox.setSelected(true);
        }
    }

}
