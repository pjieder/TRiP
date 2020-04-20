/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class RegisterFormController implements Initializable {

    @FXML
    private JFXCheckBox adminCheckbox;
    @FXML
    private JFXCheckBox userCheckbox;
    @FXML
    private JFXTextField testEmailTextField;
    @FXML
    private JFXButton registerButton;
    @FXML
    private TextField emailField;
    @FXML
    private Label invalidEmail;

    private JFXTextField field;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^.+@[^\\.].*\\.[a-z]{2,}$");
        regex.setMessage("Input is not a valid email");

        //JFX design
//        testEmailTextField.getValidators().add(regex);
//        testEmailTextField.textProperty().addListener((observable, oldValue, newValue)->{
//            registerButton.setDisable(!testEmailTextField.validate());
//        });

        //Standard Java design
        field = new JFXTextField();
        field.getValidators().add(regex);
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            field.setText(newValue);
            registerButton.setDisable(!field.validate());
            invalidEmail.setVisible(!field.validate());
        });

    }

    @FXML
    private void doAdmin(ActionEvent event) {

        if (adminCheckbox.isSelected()) {
            userCheckbox.setSelected(false);
        } else {
            userCheckbox.setSelected(true);
        }

    }

    @FXML
    private void doUser(ActionEvent event) {

        if (userCheckbox.isSelected()) {
            adminCheckbox.setSelected(false);
        } else {
            adminCheckbox.setSelected(true);
        }

    }

}
