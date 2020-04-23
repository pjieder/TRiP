/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.utilities;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trip.gui.AppModel;
import trip.gui.controllers.MainUserViewController;

/**
 *
 * @author ander
 */
public class StageOpener {

    public static void openNewStage(String FXMLPlacement) {
        
        try{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Scene scene = null;
        fxmlLoader.setLocation(AppModel.class.getResource(FXMLPlacement));
        scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        }
        catch (IOException exception)
        {
        }
    }

    public static void changeStage(String FXMLPlacement, Stage stage)
    {
        try{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Scene scene = null;
        fxmlLoader.setLocation(AppModel.class.getResource(FXMLPlacement));
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        }
        catch (IOException exception)
        {
        }
    }
    
}
