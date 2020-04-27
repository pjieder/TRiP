/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trip.be.Project;
import trip.gui.AppModel;
import trip.gui.models.ProjectModel;
import trip.utilities.StageOpener;
import trip.utilities.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class MainAdminViewController implements Initializable
{

    private AppModel appModel = new AppModel();
    private ProjectModel projectModel = new ProjectModel();

    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, String> projectColumn;
    @FXML
    private TableColumn<Project, String> timeColumn;
    @FXML
    private TableColumn<Project, String> rateColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        projectColumn.setCellValueFactory((data) ->
        {
            Project project = data.getValue();
            return new SimpleStringProperty(project.getName());
        });

        timeColumn.setCellValueFactory((data) ->
        {

            Project project = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertSecondsToString(project.getTotalTime()));
        });

        rateColumn.setCellValueFactory((data) ->
        {

            Project project = data.getValue();
            return new SimpleStringProperty(Double.toString(project.getRate()));
        });

        projectTable.setOnSort((event) ->
        {
            projectTable.getSelectionModel().clearSelection();
        });

        loadAllProjects();
    }

    public void loadAllProjects()
    {
        updateView().start();
    }

    @FXML
    private void openProject(MouseEvent event) throws IOException
    {

        if (event.getClickCount() > 1 & !projectTable.getSelectionModel().isEmpty() & !event.isConsumed())
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AppModel.class.getResource("views/MainUserView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) projectTable.getScene().getWindow();
            MainUserViewController controller = fxmlLoader.getController();
            controller.setAdmin(projectTable.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void openProjectMenu(MouseEvent event)
    {
        StageOpener.changeStage("views/MainUserView.fxml", (Stage) projectTable.getScene().getWindow());
    }

    @FXML
    private void log_out(MouseEvent event)
    {
        StageOpener.changeStage("views/Login.fxml", (Stage) projectTable.getScene().getWindow());
    }

    @FXML
    private void openUsers(ActionEvent event)
    {
        StageOpener.changeStage("views/AdminCurrentUserView.fxml", (Stage) projectTable.getScene().getWindow());
    }

    @FXML
    private void createProject(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/AddEditProject.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        AddEditProjectController controller = fxmlLoader.getController();
        controller.setUpdateThread(updateView());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void editProject(ActionEvent event) throws IOException
    {
        if (!projectTable.getSelectionModel().isEmpty())
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AppModel.class.getResource("views/AddEditProject.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            AddEditProjectController controller = fxmlLoader.getController();
            controller.setProject(updateView(), projectTable.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }
    }

    private Thread updateView()
    {
        Thread updateThread = new Thread(() ->
        {
            Platform.runLater(() ->
            {
                projectTable.setItems(projectModel.loadAllActiveProjects(LoginController.loggedUser.getId()));
                projectTable.refresh();
            });
        });

        return updateThread;
    }
}
