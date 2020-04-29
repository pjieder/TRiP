/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import trip.be.Employee;
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
    private ObservableList<Project> projects = FXCollections.observableArrayList();
    private boolean isLastOnActive = true;

    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, String> projectColumn;
    @FXML
    private TableColumn<Project, String> timeColumn;
    @FXML
    private TableColumn<Project, String> rateColumn;
    @FXML
    private Label inactiveProjects;
    @FXML
    private Label activeProjects;
    @FXML
    private ImageView activeArrow;
    @FXML
    private ImageView inactiveArrow;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private TextField searchBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        projectColumn.setCellValueFactory((data)
                ->
        {
            Project project = data.getValue();
            return new SimpleStringProperty(project.getName());
        });

        timeColumn.setCellValueFactory((data)
                ->
        {

            Project project = data.getValue();
            return new SimpleStringProperty(TimeConverter.convertSecondsToString(project.getTotalTime()));
        });

        rateColumn.setCellValueFactory((data)
                ->
        {

            Project project = data.getValue();
            return new SimpleStringProperty(Double.toString(project.getRate()));
        });

        projectTable.setOnSort((event)
                ->
        {
            projectTable.getSelectionModel().clearSelection();
        });

        loadAllProjects();
    }

    public void loadAllProjects()
    {
        projects = projectModel.loadAllActiveProjects();
        projectTable.setItems(projects);
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
        Thread updateThread = new Thread(()
                ->
        {
            Platform.runLater(()
                    ->
            {
                if (isLastOnActive == true)
                {
                    projects = projectModel.loadAllActiveProjects();
                } else
                {
                    projects = projectModel.loadAllInactiveProjects();
                }
                projectTable.setItems(projects);
                projectTable.refresh();
                search();
            });
        });

        return updateThread;
    }

    @FXML
    private void showInactiveProjects(MouseEvent event)
    {
        inactiveProjects.setVisible(false);
        activeProjects.setVisible(true);
        activeArrow.setVisible(false);
        inactiveArrow.setVisible(true);
        deleteButton.setVisible(true);
        isLastOnActive = false;
        projects = projectModel.loadAllInactiveProjects();
        projectTable.setItems(projects);
        projectTable.refresh();
        search();
    }

    @FXML
    private void showActiveProjects(MouseEvent event)
    {
        inactiveProjects.setVisible(true);
        activeProjects.setVisible(false);
        activeArrow.setVisible(true);
        inactiveArrow.setVisible(false);
        deleteButton.setVisible(false);
        isLastOnActive = true;
        projects = projectModel.loadAllActiveProjects();
        projectTable.setItems(projects);
        projectTable.refresh();
        search();
    }

    @FXML
    private void deleteProject(ActionEvent event)
    {

        if (!projectTable.getSelectionModel().isEmpty())
        {

            Project selectedProject = projectTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Confirm delete");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this project? All logged time will no longer be accessable. Proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                projectTable.getItems().remove(selectedProject);
                projectModel.deleteProject(selectedProject);
            } else
            {
                alert.close();
            }

        }

    }

    @FXML
    private void projectSearch(KeyEvent event)
    {
        search();
    }

    private void search()
    {
        String projectName = searchBar.getText();

        if (projectName.equalsIgnoreCase(""))
        {
            projectTable.setItems(projects);
        } else
        {
            projectTable.setItems(projectModel.searchProjects(projectName, projects));
        }
    }

    @FXML
    private void openCustomers(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/AdminCustomerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) projectTable.getScene().getWindow();
        AdminCustomerViewController controller = fxmlLoader.getController();
        controller.loadAllCustomers();
        stage.setScene(scene);
        stage.show();
    }
}
