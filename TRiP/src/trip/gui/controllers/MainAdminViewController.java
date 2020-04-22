/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import trip.be.Admin;
import trip.be.Employee;
import trip.be.Project;
import trip.gui.AppModel;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MainAdminViewController implements Initializable {

    AppModel appModel = new AppModel();
    private Admin loggedAdmin;

    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, String> projectColumn;
    @FXML
    private TableColumn<Project, String> timeColumn;
    @FXML
    private JFXListView<Employee> employeeList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        projectColumn.setCellValueFactory((data) -> {
            Project project = data.getValue();
            return new SimpleStringProperty(project.getName());
        });

        timeColumn.setCellValueFactory((data) -> {

            Project project = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertSecondsToString(project.getTotalTime()));
        });

        loadUsers();
    }

    public void setAdmin(Admin admin) {
        loggedAdmin = admin;
        projectTable.setItems(loggedAdmin.getProjects());
    }

    public void loadUsers() {
        employeeList.setItems(appModel.loadUsers());
    }

}
