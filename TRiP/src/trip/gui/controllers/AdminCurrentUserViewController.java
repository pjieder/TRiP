/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import trip.be.Employee;
import trip.be.Project;
import trip.gui.models.EmployeeModel;
import trip.gui.TRiP;
import trip.gui.models.ProjectModel;
import trip.utilities.JFXAlert;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AdminCurrentUserViewController implements Initializable {

    private EmployeeModel employeeModel = new EmployeeModel();
    private ProjectModel projectModel = new ProjectModel();
    private boolean isLastOnActive = true;
    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    @FXML
    private JFXListView<Employee> userList;
    @FXML
    private Label inactiveUsers;
    @FXML
    private ImageView activeArrow;
    @FXML
    private Label activeUsers;
    @FXML
    private ImageView inactiveArrow;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXButton makeInactivebtn;
    @FXML
    private JFXButton makeActivebtn;
    @FXML
    private ComboBox<Project> projectComboBox;
    @FXML
    private TextField searchBar;
    @FXML
    private StackPane stackPane;
    @FXML
    private StackPane stackPaneDelete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadUsers();
    }

    /**
     * Loads the active employees into the user list and loads the combobox with all active projects.
     */
    public void loadUsers() {
        try {
            employees = employeeModel.loadActiveEmployees();
            userList.setItems(employees);
            projectComboBox.setItems(projectModel.loadAllActiveProjects());
            projectComboBox.getItems().add(0, new Project("All projects", 0));
            projectComboBox.getSelectionModel().select(0);
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error loading users.");
        }
    }

    /**
     * Opens the RegisterForm FXML view as a new stage in order to create users.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void openAddUser(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(TRiP.class.getResource("views/RegisterFormView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("TRiP");
            stage.getIcons().add(new Image(TRiP.class.getResourceAsStream("images/time.png")));

            RegisterFormController controller = fxmlLoader.getController();
            controller.setUpdateThread(getUpdateListThread());

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            JFXAlert.openError(stackPane, "Error opening create form.");
        }
    }

    /**
     * Opens the RegisterForm FXML view as a new stage and inserts the data already stored about the employee
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void openUpdateUser(ActionEvent event) {

        if (!userList.getSelectionModel().isEmpty()) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(TRiP.class.getResource("views/RegisterFormView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setTitle("TRiP");
                stage.getIcons().add(new Image(TRiP.class.getResourceAsStream("images/time.png")));

                RegisterFormController controller = fxmlLoader.getController();
                controller.setEmployee(userList.getSelectionModel().getSelectedItem(), getUpdateListThread());

                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                 JFXAlert.openError(stackPane, "Error opening update form.");
            }
        }
    }

    /**
     * Loads all inactive employees and inserts them into the user list.
     *
     * @param event
     */
    @FXML
    private void showInactiveUsers(MouseEvent event) {
        inactiveUsers.setVisible(false);
        activeUsers.setVisible(true);
        activeArrow.setVisible(false);
        inactiveArrow.setVisible(true);
        deleteButton.setVisible(true);
        makeInactivebtn.setVisible(false);
        makeActivebtn.setVisible(true);
        isLastOnActive = false;
        getUpdateListThread().start();
    }

    /**
     * Loads all active employees and inserts them into the user list.
     *
     * @param event
     */
    @FXML
    private void showActiveUsers(MouseEvent event) {
        inactiveUsers.setVisible(true);
        activeUsers.setVisible(false);
        activeArrow.setVisible(true);
        inactiveArrow.setVisible(false);
        deleteButton.setVisible(false);
        makeInactivebtn.setVisible(true);
        makeActivebtn.setVisible(false);
        isLastOnActive = true;
        getUpdateListThread().start();
    }

    /**
     * Disables the selected employee making them inactive.
     *
     * @param event
     */
    @FXML
    private void makeUserInactive(ActionEvent event) {
        try {
            if (!userList.getSelectionModel().isEmpty()) {
                employeeModel.updateEmployeeActive(userList.getSelectionModel().getSelectedItem(), false);
                getUpdateListThread().start();
            }
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error making user inactive.");
        }
    }

    /**
     * Activates the selected employee making them active.
     *
     * @param event
     */
    @FXML
    private void makeUserActive(ActionEvent event) {
        try {
            if (!userList.getSelectionModel().isEmpty()) {
                employeeModel.updateEmployeeActive(userList.getSelectionModel().getSelectedItem(), true);
                getUpdateListThread().start();
            }
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error making user active.");
        }
    }

    /**
     * Deletes the selected employee from the system together with registered time, project and tasks for the selected employee.
     *
     * @param event
     */
    @FXML
    private void deleteUser(ActionEvent event) {

        if (!userList.getSelectionModel().isEmpty()) {

            Employee selectedEmployee = userList.getSelectionModel().getSelectedItem();
            String message = "Are you sure you want to delete employee: " + selectedEmployee.getfName() + "? All logged time by the user will no longer be accessable. Proceed?";

            Thread confirmExecuteThread = new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        employees.remove(selectedEmployee);
                    });
                    employeeModel.deleteEmployee(selectedEmployee);
                } catch (SQLException ex) {
                    Platform.runLater(() -> {
                        JFXAlert.openError(stackPane, "Error deleting user.");
                    });
                }
            });
            JFXAlert.openConfirm(stackPaneDelete, message, confirmExecuteThread);
        }
    }

    /**
     * Creates a new Thread that updates the data stored in the project list.
     *
     * @return the update Thread to be executed
     */
    private Thread getUpdateListThread() {
        Thread updateThread = new Thread(() -> {

            Platform.runLater(() -> {
                setProject();
                search();
            });
        });
        return updateThread;
    }
    
    /**
     * Event handler for the project combobox. Runs method setProject and search in order to update the view.
     *
     * @param event
     */
    @FXML
    private void sortProject(ActionEvent event) {
        setProject();
        search();
    }

    /**
     * Event handler for the search bar. Runs method search in order to update the view.
     *
     * @param event
     */
    @FXML
    private void userSearch(KeyEvent event) {
        search();
    }

    /**
     * Loads all employees in the userlist that works on the selected project.
     */
    private void setProject() {
        try {
            Project project = projectComboBox.getValue();

            if (project.getId() == 0) {
                if (isLastOnActive) {
                    employees = employeeModel.loadActiveEmployees();
                } else {
                    employees = employeeModel.loadInactiveEmployees();
                }
            } else {
                employees = employeeModel.loadEmployeesAssignedToProject(project.getId(), isLastOnActive);
            }
        } catch (SQLException ex) {
            JFXAlert.openError(stackPane, "Error loading employees.");
        }
    }

    /**
     * Searches through the userlist and displays employees mathing the name of the search term.
     */
    private void search() {
        String userName = searchBar.getText();

        if (userName.equalsIgnoreCase("")) {
            userList.setItems(employees);
        } else {
            userList.setItems(employeeModel.searchEmployee(userName, employees));
        }
    }

}
