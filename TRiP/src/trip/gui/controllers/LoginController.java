/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trip.be.Employee;
import trip.be.Roles;
import trip.gui.AppModel;

/**
 * FXML Controller class
 *
 * @author ander
 */
public class LoginController implements Initializable {

    private AppModel appModel = new AppModel();
    private Preferences preferences;
    public static Employee loggedUser;

    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXCheckBox rememberMe;
    @FXML
    private JFXButton loginButton;
    @FXML
    private Label errorMsg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        preferences = Preferences.userRoot().node("TRiP");
        if (preferences != null) {
            usernameField.setText(preferences.get("username", null));
            passwordField.setText(preferences.get("password", null));
            rememberMe.setSelected(preferences.getBoolean("rememberActivated", false));
        }

    }

    /**
     * Event handler for the login button. Starts the thread returned by the loadPerson method.
     *
     * @param event
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @FXML
    private void login(MouseEvent event) throws IOException, InterruptedException, ExecutionException {
        loadPerson().start();
        rememberMe.requestFocus();
    }

    /**
     * Displays loading and disables the login button
     */
    private void showLoading() {
        loginButton.setDisable(true);
        progressBar.setVisible(true);
        errorMsg.setVisible(false);
    }

    /**
     * Hides loading and enables the login button
     */
    private void hideLoading() {
        progressBar.setVisible(false);
        errorMsg.setVisible(true);
        loginButton.setDisable(false);
    }

    /**
     * Saves the login username and password entered in preferences
     */
    private void saveLogin() {
        preferences.put("username", usernameField.getText());
        preferences.put("password", passwordField.getText());
        preferences.putBoolean("rememberActivated", true);
    }

    /**
     * Saves an empty string as the username and password in preferences
     */
    private void forgetLogin() {
        preferences.put("username", "");
        preferences.put("password", "");
        preferences.putBoolean("rememberActivated", false);
    }

    public Thread loadPerson() throws InterruptedException, ExecutionException, IOException {

        Thread loginThread = new Thread(() -> {

            String username = usernameField.getText();
            String password = passwordField.getText();
            showLoading();

            Employee employeeToValidate = appModel.validateEmployee(username, password);

            if (employeeToValidate == null) {
                hideLoading();
            } else {

                if (rememberMe.isSelected()) {
                    saveLogin();
                } else {
                    forgetLogin();
                }
                loggedUser = employeeToValidate;
                Platform.runLater(() -> {

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(AppModel.class.getResource("views/MenuBarView.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage stage = (Stage) rememberMe.getScene().getWindow();
                        MenuBarViewController controller = fxmlLoader.getController();

                        if (employeeToValidate.getRole().equals(Roles.USER)) {
                            controller.setUser(stage, scene);
                        } else if (employeeToValidate.getRole().equals(Roles.ADMIN)) {
                            controller.setAdmin(stage, scene);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
        return loginThread;
    }

}
