/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import trip.gui.models.ProjectModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AdminStatisticsViewController implements Initializable {

    private ProjectModel projectModel = new ProjectModel();
    
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private JFXButton calculate;
    @FXML
    private JFXDatePicker date1;
    @FXML
    private JFXDatePicker date2;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
              lineChart.getData().clear();
    }    

    @FXML
    private void calculate(ActionEvent event) {
        
        LocalDate localDateFirst = date1.getValue();
        LocalDate localDateLast = date2.getValue();
        lineChart.getData().clear();
        lineChart.getData().add(projectModel.calculateGraph(1, localDateFirst, localDateLast));
        
        
    }
    
}
