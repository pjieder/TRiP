/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import trip.be.Project;
import trip.gui.models.ProjectModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AddEditProjectController implements Initializable
{
    private ProjectModel projectModel;

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField clientField;
    @FXML
    private JFXTextField rateField;
    @FXML
    private ComboBox<?> activeUsersCBox;
    @FXML
    private JFXListView<?> activeUsersList;
    @FXML
    private JFXButton add;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        projectModel = ProjectModel.getInstance();
    }

    @FXML
    private void addProject(ActionEvent event)
    {
        Project project = new Project();
        project.setName(nameField.getText());
        project.setRate(Double.parseDouble(rateField.getText()));
//        if(activeUsersList == null)
//        {
//            project.setIsActive(false);
//        }
//        else
//        {
            project.setIsActive(true);
//        }
        projectModel.createProject(project);
        
        Stage stage = (Stage) add.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void editProject(ActionEvent event)
    {
    }

    @FXML
    private void deleteProject(ActionEvent event)
    {
    }

}
