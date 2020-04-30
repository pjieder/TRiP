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
import javafx.scene.control.ComboBox;
import trip.be.Project;
import trip.gui.models.ProjectModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AdminStatisticsViewController implements Initializable {

    private ProjectModel projectModel = new ProjectModel();
    
    @FXML
    private LineChart<String, Double> lineChart;
    @FXML
    private JFXButton calculate;
    @FXML
    private ComboBox<?> statisticComboBox;
    @FXML
    private JFXDatePicker dateEnd;
    @FXML
    private JFXDatePicker dateStart;
    @FXML
    private ComboBox<Project> projectComboBox;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        projectComboBox.setItems(projectModel.loadAllActiveProjects());
        projectComboBox.getItems().add(0, new Project("All projects", 0));
        projectComboBox.getSelectionModel().select(0);
        
    }    

    @FXML
    private void calculate(ActionEvent event) {
        
        LocalDate localDateFirst = dateStart.getValue();
        LocalDate localDateLast = dateEnd.getValue();
        Project selectedProject = projectComboBox.getValue();
        lineChart.getData().clear();
        lineChart.getData().add(projectModel.calculateGraph(selectedProject.getId(), localDateFirst, localDateLast));
        
        
    }

    @FXML
    private void changeStatistic(ActionEvent event) {
    }

    @FXML
    private void validate(ActionEvent event) {
        
        if (dateStart.getValue() != null && dateEnd.getValue() != null)
        {
            calculate.setDisable(false);
        }
        else
        {
            calculate.setDisable(true);
        }
        
    }
    
}
