/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import trip.be.Employee;
import trip.be.Project;
import trip.gui.AppModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AdminCurrentUserViewController implements Initializable {

    private AppModel appModel = new AppModel();
    private boolean isLastOnActive = true;

    @FXML
    private JFXListView<Employee> userList;
    @FXML
    private Label inactiveUsers;
    @FXML
    private ImageView activeArrow;
    @FXML
    private Label activeUsers;
    @FXML
    private ImageView inactiveArrow;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton makeInactivebtn;
    @FXML
    private JFXButton makeActivebtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadUsers();
    }

    public Thread getUpdateListThread() {
        Thread updateThread = new Thread(() -> {

            Platform.runLater(() -> {
                if (isLastOnActive == true) {
                    userList.setItems(appModel.loadActiveUsers());
                } else {
                    userList.setItems(appModel.loadInactiveUsers());
                }
                userList.refresh();
            });

        });

        return updateThread;
    }

    public void loadUsers() {
        userList.setItems(appModel.loadActiveUsers());
    }

    @FXML
    private void open_projects_view(MouseEvent event) {
    }

    @FXML
    private void open_users_view(MouseEvent event) {
    }

    @FXML
    private void open_statistics_view(MouseEvent event) {
    }

    @FXML
    private void open_time_view(MouseEvent event) {
    }

    @FXML
    private void log_out(MouseEvent event) {
    }

    @FXML
    private void openAddUser(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/RegisterForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        RegisterFormController controller = fxmlLoader.getController();
        controller.setUpdateThread(getUpdateListThread());

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openUpdateUser(ActionEvent event) throws IOException {

        if (!userList.getSelectionModel().isEmpty()) {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AppModel.class.getResource("views/RegisterForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            RegisterFormController controller = fxmlLoader.getController();
            controller.setEmployee(userList.getSelectionModel().getSelectedItem(), getUpdateListThread());

            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void showInactiveUsers(MouseEvent event) {
        inactiveUsers.setVisible(false);
        activeUsers.setVisible(true);
        activeArrow.setVisible(false);
        inactiveArrow.setVisible(true);
        deleteButton.setVisible(true);
        makeInactivebtn.setVisible(false);
        makeActivebtn.setVisible(true);
        isLastOnActive = false;
        userList.setItems(appModel.loadInactiveUsers());
        userList.refresh();
    }

    @FXML
    private void showActiveUsers(MouseEvent event) {
        inactiveUsers.setVisible(true);
        activeUsers.setVisible(false);
        activeArrow.setVisible(true);
        inactiveArrow.setVisible(false);
        deleteButton.setVisible(false);
        makeInactivebtn.setVisible(true);
        makeActivebtn.setVisible(false);
        isLastOnActive = true;
        userList.setItems(appModel.loadActiveUsers());
        userList.refresh();
    }

    @FXML
    private void makeUserInactive(ActionEvent event) {

        if (!userList.getSelectionModel().isEmpty()) {
            appModel.updateEmployeeActive(userList.getSelectionModel().getSelectedItem().getId(), false);
            getUpdateListThread().start();
        }

    }

    @FXML
    private void makeUserActive(ActionEvent event) {

        if (!userList.getSelectionModel().isEmpty()) {
            appModel.updateEmployeeActive(userList.getSelectionModel().getSelectedItem().getId(), true);
            getUpdateListThread().start();
        }
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        if (!userList.getSelectionModel().isEmpty()) {

            Employee selectedEmployee = userList.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Confirm delete");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this project? All logged time will no longer be accessable. Proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                userList.getItems().remove(selectedEmployee);
                appModel.deleteEmployee(selectedEmployee);
            } else {
            }

        }
    }
}
