/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui;

import trip.dal.dbaccess.DBSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Peter
 */
public class TRiP extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("views/LoginView.fxml"));
      
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        stage.setTitle("TRiP");
        stage.getIcons().add(new Image(TRiP.class.getResourceAsStream("images/time.png")));
        stage.setResizable(false);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            DBSettings.getInstance().closeAllConnections();
        }));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
