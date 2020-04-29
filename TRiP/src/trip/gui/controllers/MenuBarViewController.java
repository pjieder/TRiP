/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openProjectsView(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("MainAdminView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openCustomerView(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("AdminCustomerView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openUserView(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("AdminCurrentUserView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openStatisticsView(MouseEvent event) throws IOException, IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("AdminStatisticsView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openTimeTrackingView(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("MainUserView.fxml"));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(fxml);
    }

    @FXML
    private void openEconomyView(MouseEvent event) throws IOException {
        
    }

    @FXML
    private void logOut(MouseEvent event) {
    }
    
}
