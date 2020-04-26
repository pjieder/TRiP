/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MenuBarViewController implements Initializable {

    @FXML
    private BorderPane BP;
    @FXML
    private AnchorPane AP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openProjectsView(MouseEvent event) {
        BP.setCenter(AP);
    }

    @FXML
    private void openUserView(MouseEvent event) {

    }

    @FXML
    private void openStatisticsView(MouseEvent event) {

    }

    @FXML
    private void openTimeTrackingView(MouseEvent event) {

    }

    @FXML
    private void openEconomyView(MouseEvent event) {
    }

    @FXML
    private void logOut(MouseEvent event) {
    }
    
    private void loadPage(String page) {
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource(page+".fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MenuBarViewController.class.getName()).log(Level.SEVERE, null, ex);
        BP.setCenter(root);
    }
    }
           
    
}
