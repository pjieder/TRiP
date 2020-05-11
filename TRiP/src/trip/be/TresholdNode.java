/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.be;

import java.text.DecimalFormat;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author ander
 */
public class TresholdNode extends StackPane {

    private DecimalFormat df = new DecimalFormat("0.0#");
    
    /**
     *
     * @param value
     */
    public TresholdNode(double value) {
        setPrefSize(15, 15);

        final Tooltip tooltip = new Tooltip();
        tooltip.setText(df.format(value));
        tooltip.showDelayProperty().set(Duration.ZERO);
        tooltip.setStyle("-fx-font-size: 15px; -fx-background-color: rgb(154, 128, 254, .8);");
        
        Tooltip.install(this, tooltip);

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setCursor(Cursor.NONE);
                toFront();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setCursor(Cursor.CROSSHAIR);
            }
        });
    }

}
