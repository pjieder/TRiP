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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import trip.gui.TRiP;
import trip.utilities.JFXAlert;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MenuBarViewController implements Initializable {

    public static Pane viewPane;
    
    @FXML
    private AnchorPane AP;
    @FXML
    private Pane pane;
    @FXML
    private VBox vBox;
    @FXML
    private JFXButton logOutTab;
    @FXML
    private StackPane stackPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * If an admin logs in, the pane view within the MenuBarView is set to MainAdminView.
     * @param stage The stage of the login.
     * @param scene The scene of the login display.
     */
    public void setAdmin(Stage stage, Scene scene){
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/MainAdminView.fxml"));
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
                Platform.runLater(() -> {
                    JFXAlert.openError(stackPane, "Error loading Admin view.");
                });
            }
        });
        thread.start();
    }

    /**
     * If a user logs in, the pane view within the MenuBarView is set to MainUserView
     * @param stage The stage of the login.
     * @param scene The scene of the login display.
     */
    public void setUser(Stage stage, Scene scene){
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/MainUserView.fxml"));
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
                Platform.runLater(() -> {
                    JFXAlert.openError(stackPane, "Error loading user view.");
                });
            }
        });
        thread.start();
    }

    /**
     * Opens the MainAdminView within the menubar view.
     * @param event 
     */
    @FXML
    private void openProjectsView(MouseEvent event){
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/MainAdminView.fxml"));
                Parent fxml = fxmlLoader.load();
                MainAdminViewController controller = fxmlLoader.getController();
                controller.loadAllProjects();
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    JFXAlert.openError(stackPane, "Error loading main Admin view.");
                });
            }
        });
        thread.start();
    }

    /**
     * Opens the AdminCustomerView within the menubar view.
     * @param event 
     */
    @FXML
    private void openCustomerView(MouseEvent event){
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/AdminCustomerView.fxml"));
                Parent fxml = fxmlLoader.load();
                AdminCustomerViewController controller = fxmlLoader.getController();
                controller.loadAllCustomers();
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    JFXAlert.openError(stackPane, "Error loading customer view.");
                });

            }
        });
        thread.start();
    }

    /**
     * Opens the AdminCurrentUserView view within the menubar view.
     * @param event 
     */
    @FXML
    private void openUserView(MouseEvent event){
        Thread thread = new Thread(() -> {
            try {
                Parent fxml = FXMLLoader.load(TRiP.class.getResource("views/AdminCurrentUserView.fxml"));
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                Platform.runLater(()->{JFXAlert.openError(stackPane, "Error loading main user view.");});
            }
        });
        thread.start();
    }

    /**
     * Opens the AdminStatisticsView within the menubar view.
     * @param event 
     */
    @FXML
    private void openStatisticsView(MouseEvent event){
        Thread thread = new Thread(() -> {
            try {
                Parent fxml = FXMLLoader.load(TRiP.class.getResource("views/AdminStatisticsView.fxml"));
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                Platform.runLater(()->{JFXAlert.openError(stackPane, "Error loading statistics view.");});
            }
        });
        thread.start();
    }

    /**
     * Opens the MainUserView within the menubar view.
     * @param event 
     */
    @FXML
    private void openTimeTrackingView(MouseEvent event){
        Thread thread = new Thread(() -> {
            try {
                Parent fxml = FXMLLoader.load(TRiP.class.getResource("views/MainUserView.fxml"));
                Platform.runLater(() -> {
                    pane.getChildren().removeAll();
                    pane.getChildren().setAll(fxml);
                });
            } catch (IOException ex) {
                Platform.runLater(()->{JFXAlert.openError(stackPane, "Error loading time tracking view.");});
            }
        });
        thread.start();
    }

    /**
     * Logs the current employee out of the system.
     * @param event 
     */
    @FXML
    private void logOut(MouseEvent event){
        Thread thread = new Thread(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/LoginView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) AP.getScene().getWindow();
                Platform.runLater(() -> {

                    stage.setScene(scene);
                    stage.show();
                });
            } catch (IOException ex) {
                Platform.runLater(()->{JFXAlert.openError(stackPane, "Error logging out.");});
            }
        });
        thread.start();
    }

}
