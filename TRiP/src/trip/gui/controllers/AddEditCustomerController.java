/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import trip.be.Customer;
import trip.gui.models.CustomerModel;

/**
 * FXML Controller class
 *
 * @author Jacob
 */
public class AddEditCustomerController implements Initializable
{
    private CustomerModel customerModel = new CustomerModel();
    
    private Thread updateThread;
    private Customer customerToUpdate;

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField phoneNumberField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXButton registerButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton updateButton;
    @FXML
    private Label title;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^.+@[^\\.].*\\.[a-z]{2,}$");
        regex.setMessage("Input is not a valid email");

        nameField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateInput();
        });

        phoneNumberField.textProperty().addListener((Observable, oldValue, newValue) ->
        {
            validateInput();
        });

        emailField.getValidators().add(regex);
        emailField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            emailField.validate();
            validateInput();
        });
    }

    private void validateInput()
    {
        String name = nameField.getText().trim();

        if (name.equals(""))
        {
            registerButton.setDisable(true);
            updateButton.setDisable(true);
        } 
         else
        {
            registerButton.setDisable(false);
            updateButton.setDisable(false);
        }
    }

    public void setUpdateThread(Thread thread) {
        this.updateThread = thread;
    }
    
    public void setCustomer(Customer customer, Thread thread) {
        title.setText("Update Customer");
        
        this.updateThread = thread;
        this.customerToUpdate = customer;
        
        registerButton.setVisible(false);
        updateButton.setVisible(true);
        
        nameField.setText(customer.getName());
        if (!customer.getPhoneNumber().equals("")){phoneNumberField.setText(customer.getPhoneNumber());}
        if (!customer.getEmail().equals("")){emailField.setText(customer.getEmail());}
        validateInput();
    }
    
    @FXML
    private void registerCustomer(ActionEvent event)
    {
        Customer customer = new Customer();
        
        customer.setName(nameField.getText());
        customer.setPhoneNumber(phoneNumberField.getText());
        customer.setEmail(emailField.getText());

        if (customerToUpdate == null) {
            customerModel.createCustomer(customer);
        } else {
            customer.setId(customerToUpdate.getId());
            customerModel.updateCustomer(customer);
        }

        updateThread.start();
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void cancelScene(ActionEvent event)
    {
        Stage currentStage = (Stage) nameField.getScene().getWindow();
        currentStage.close();
    }

}
