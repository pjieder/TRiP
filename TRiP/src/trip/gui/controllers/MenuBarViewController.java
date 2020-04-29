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

    public void setAdmin() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/MainAdminView.fxml"));
        Pane adminPane = fxmlLoader.load();
        MainAdminViewController controller = fxmlLoader.getController();
        controller.loadAllProjects();
        pane.getChildren().add(adminPane);
        viewPane = this.pane;
    }

    public void setUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/MainUserView.fxml"));
        Pane userPane = fxmlLoader.load();
        pane.getChildren().add(userPane);
        viewPane = this.pane;
        vBox.getChildren().removeAll();
        vBox.getChildren().setAll(logOutTab);
    }

    @FXML
    private void openProjectsView(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/MainAdminView.fxml"));
        Parent fxml = fxmlLoader.load();
        MainAdminViewController controller = fxmlLoader.getController();
        controller.loadAllProjects();
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openCustomerView(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/AdminCustomerView.fxml"));
        Parent fxml = fxmlLoader.load();
        AdminCustomerViewController controller = fxmlLoader.getController();
        controller.loadAllCustomers();
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openUserView(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(AppModel.class.getResource("views/AdminCurrentUserView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openStatisticsView(MouseEvent event) throws IOException, IOException {
        Parent fxml = FXMLLoader.load(AppModel.class.getResource("views/AdminStatisticsView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openTimeTrackingView(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(AppModel.class.getResource("views/MainUserView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openEconomyView(MouseEvent event) throws IOException {

    }

    @FXML
    private void logOut(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) AP.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
