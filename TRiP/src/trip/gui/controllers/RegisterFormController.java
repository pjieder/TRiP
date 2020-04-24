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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import trip.be.Admin;
import trip.be.Employee;
import trip.be.Roles;
import trip.be.User;
import trip.gui.AppModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class RegisterFormController implements Initializable {
    
    private AppModel appModel = new AppModel();
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
    private JFXButton cancelButton;
    @FXML
    private JFXCheckBox passwordVisibility;

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
    
    private void validateInput() {
        
        String fName = firstNameField.getText();
        String lName = lastNameField.getText();
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
    
    @FXML
    private void registerEmployee(ActionEvent event) {
        Employee newEmployee;
        
        String fName = firstNameField.getText().trim();
        String lName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (adminCheckbox.isSelected())
        {
            newEmployee = new Admin(fName, lName, email);
            
        } else
        {
            newEmployee = new User(fName, lName, email);
        }
        
        if (employeeToUpdate == null)
        {
            appModel.createUser(newEmployee, password);
        } else
        {
            newEmployee.setId(employeeToUpdate.getId());
            appModel.updateEmployee(newEmployee);
            
            if (passwordVisibility.isSelected())
            {
                appModel.updatePassword(email, password, newEmployee.getId());
            }
        }
        
        updateThread.run();
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        currentStage.close();
    }

    
    @FXML
    private void cancelScene(ActionEvent event) {
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        currentStage.close();
    }
    
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
    
    public void setUpdateThread(Thread thread) {
        this.updateThread = thread;
    }
    
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
    
    @FXML
    private void doAdmin(ActionEvent event) {
        
        if (adminCheckbox.isSelected())
        {
            userCheckbox.setSelected(false);
        } else
        {
            userCheckbox.setSelected(true);
        }
        
    }
    
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
