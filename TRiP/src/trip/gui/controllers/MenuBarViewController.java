/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import trip.gui.AppModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MenuBarViewController implements Initializable {

    @FXML
    private AnchorPane AP;
    @FXML
    private Pane pane;
    @FXML
    private VBox vBox;
    @FXML
    private JFXButton projectTab;
    @FXML
    private JFXButton customersTab;
    @FXML
    private JFXButton usersTab;
    @FXML
    private JFXButton statisticsTab;
    @FXML
    private JFXButton timeTrackingTab;
    @FXML
    private JFXButton EconomyTab;
    @FXML
    private JFXButton logOutTab;

    public static Pane viewPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /*
    If an admin logs in, the pane view within the MenuBarView is set to MainAdminView
    */
    public void setAdmin(Stage stage, Scene scene) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(AppModel.class.getResource("views/MainAdminView.fxml"));
                Pane adminPane = fxmlLoader.load();
                MainAdminViewController controller = fxmlLoader.getController();
                controller.loadAllProjects();
                pane.getChildren().add(adminPane);

                Platform.runLater(() -> {
                    stage.setScene(scene);
                    stage.show();
                });
                viewPane = this.pane;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    /*
    If a user logs in, the pane view within the MenuBarView is set to MainUserView
    */
    public void setUser(Stage stage, Scene scene) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(AppModel.class.getResource("views/MainUserView.fxml"));
                Pane userPane = fxmlLoader.load();
                pane.getChildren().add(userPane);
                viewPane = this.pane;
                vBox.getChildren().removeAll();
                vBox.getChildren().setAll(logOutTab);

                Platform.runLater(() -> {
                    stage.setScene(scene);
                    stage.show();
                });

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    /*
    Opens the MainAdminView view within the menubar view.
    */
    @FXML
    private void openProjectsView(MouseEvent event) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(AppModel.class.getResource("views/MainAdminView.fxml"));
                Parent fxml = fxmlLoader.load();
                MainAdminViewController controller = fxmlLoader.getController();
                controller.loadAllProjects();
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    /*
    Opens the AdminCustomerView view within the menubar view.
    */
    @FXML
    private void openCustomerView(MouseEvent event) {
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(AppModel.class.getResource("views/AdminCustomerView.fxml"));
                Parent fxml = fxmlLoader.load();
                AdminCustomerViewController controller = fxmlLoader.getController();
                controller.loadAllCustomers();
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    /*
    Opens the AdminCurrentUserView view within the menubar view.
    */
    @FXML
    private void openUserView(MouseEvent event) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                Parent fxml = FXMLLoader.load(AppModel.class.getResource("views/AdminCurrentUserView.fxml"));
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    /*
    Opens the AdminStatisticsView view within the menubar view.
    */
    @FXML
    private void openStatisticsView(MouseEvent event) throws IOException, IOException {
        Thread thread = new Thread(() -> {
            try {
                Parent fxml = FXMLLoader.load(AppModel.class.getResource("views/AdminStatisticsView.fxml"));
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    /*
    Opens the MainUserView view within the menubar view.
    */
    @FXML
    private void openTimeTrackingView(MouseEvent event) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                Parent fxml = FXMLLoader.load(AppModel.class.getResource("views/MainUserView.fxml"));
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    /*
    Opens the EconomyView view within the menubar view.
    */
    @FXML
    private void openEconomyView(MouseEvent event) throws IOException {

    }

    /*
    Logs the current user/admin out.
    */
    @FXML
    private void logOut(MouseEvent event) throws IOException {
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(AppModel.class.getResource("views/Login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) AP.getScene().getWindow();
                Platform.runLater(() -> {

                    stage.setScene(scene);
                    stage.show();
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

}
