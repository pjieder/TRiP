/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRiP.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class LoginViewController implements Initializable {

    @FXML
    private JFXCheckBox rememberMe;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Label errorMsg;
    @FXML
    private JFXButton loginButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void login(MouseEvent event) {
    }
    
}
