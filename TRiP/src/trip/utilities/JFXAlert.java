/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.utilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author NLens
 */
public class JFXAlert {

    public static void openError(StackPane stackPane, String errorMessage) {
        String title = "Alert";
        stackPane.setVisible(true);
        
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(errorMessage));
        JFXButton close = new JFXButton("Close");
        JFXDialog dialog = new JFXDialog(stackPane, dialogContent, JFXDialog.DialogTransition.CENTER, false);

        close.setOnMouseClicked((event) ->
        {
            dialog.close();
            stackPane.setVisible(false);
        });

        close.setButtonType(JFXButton.ButtonType.RAISED);
        //TODO set style css
        dialogContent.setActions(close);

        dialog.show();
    }

}
