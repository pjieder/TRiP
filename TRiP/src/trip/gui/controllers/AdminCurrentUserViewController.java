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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trip.be.Employee;
import trip.gui.AppModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AdminCurrentUserViewController implements Initializable {

    private AppModel appModel = new AppModel();

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadUsers();
    }

    public Thread getUpdateListThread() {
        Thread updateThread = new Thread(() -> {
            loadUsers();
        });

        return updateThread;
    }

    public void loadUsers() {
        userList.setItems(appModel.loadUsers());
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
//        userList.setItems(projectModel.loadAllInactiveProjects());
        userList.refresh();
    }

    @FXML
    private void showActiveUsers(MouseEvent event) {
        inactiveUsers.setVisible(true);
        activeUsers.setVisible(false);
        activeArrow.setVisible(true);
        inactiveArrow.setVisible(false);
        deleteButton.setVisible(false);
        userList.setItems(appModel.loadUsers());
        userList.refresh();
    }

    @FXML
    private void deleteProject(ActionEvent event) {
    }
}
