/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trip.be.Customer;
import trip.be.Employee;
import trip.be.Project;
import trip.gui.AppModel;
import trip.gui.models.CustomerModel;
import trip.gui.models.ProjectModel;

/**
 * FXML Controller class
 *
 * @author Peter
 */
public class AddEditProjectController implements Initializable {

    private Thread updateThread;
    private Project projectToUpdate;

    private ProjectModel projectModel = new ProjectModel();
    private AppModel appModel = new AppModel();
    private CustomerModel customerModel = new CustomerModel();

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField rateField;
    @FXML
    private ChoiceBox<Employee> activeUsersCBox;
    @FXML
    private JFXListView<Employee> activeUsersList;
    @FXML
    private JFXButton registerButton;
    @FXML
    private Label removeUser;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXComboBox<Customer> customerComboBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        RegexValidator regex = new RegexValidator();
        regex.setRegexPattern("^[0-9]{1,4}([,.][0-9]{1,2})?$");
        regex.setMessage("Input is not a valid number");

        rateField.getValidators().add(regex);

        nameField.textProperty().addListener((Observable, oldValue, newValue)
                -> {
            validateInput();
        });

        rateField.textProperty().addListener((Observable, oldValue, newValue)
                -> {
            validateInput();
            rateField.validate();
        });

        activeUsersCBox.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) -> {
            addUserToProject();
        });

        activeUsersList.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    if (employee.isActive() == false) {
                        setText(employee.toString() + " [user inactive]");
                    } else {
                        setText(employee.toString());
                    }
                }
            }
        });

    }

    /**
     * Validates the entered information and enables the register- or update button if sufficient information is given.
     */
    private void validateInput() {
        if (nameField.getText().equals("") || rateField.getText().equals("")) {
            registerButton.setDisable(true);
            updateButton.setDisable(true);
        } else if (!rateField.validate() || customerComboBox.getValue() == null) {
            registerButton.setDisable(true);
            updateButton.setDisable(true);
        } else {
            registerButton.setDisable(false);
            updateButton.setDisable(false);
        }
    }

    /**
     * Saves or updates the project based on what button was pressed when opening the FXML.
     *
     * @param event
     */
    @FXML
    private void registerProject(ActionEvent event) {
        Project project = new Project();
        project.setName(nameField.getText());
        project.setRate(Double.parseDouble(rateField.getText().replace(",", ".")));
        project.setCustomer(customerComboBox.getValue());

        if (activeUsersList.getItems().isEmpty()) {
            project.setIsActive(false);
        } else {
            project.setIsActive(true);
        }

        if (projectToUpdate == null) {
            projectModel.createProject(project, activeUsersList.getItems());
        } else {
            project.setId(projectToUpdate.getId());
            projectModel.updateProject(project, activeUsersList.getItems());
        }

        updateThread.start();
        closeScene();
    }

    /**
     * Adds an active user to the list of users working on the project.
     */
    private void addUserToProject() {

        if (activeUsersCBox.getValue() != null && !activeUsersList.getItems().contains(activeUsersCBox.getSelectionModel().getSelectedItem())) {

            Employee selectedItem = activeUsersCBox.getValue();
            activeUsersCBox.getItems().clear();
            activeUsersList.getItems().add(selectedItem);
            updateCheckBox();

        }
    }

    /**
     * Updates the check box containing active employees not yet assigned to the project.
     */
    private void updateCheckBox() {
        ObservableList<Employee> updatedCombo = appModel.loadActiveEmployees();

        for (Employee employee : activeUsersList.getItems()) {
            updatedCombo.remove(employee);
        }
        activeUsersCBox.setItems(updatedCombo);
    }

    /**
     * Removes the selected employee from the list containing active employees working on the project and returns it to the check box.
     *
     * @param event
     */
    @FXML
    private void removeUser(MouseEvent event) {
        if (!activeUsersList.getSelectionModel().isEmpty()) {
            activeUsersList.getItems().remove(activeUsersList.getSelectionModel().getSelectedItem());
            updateCheckBox();
        }
    }

    /**
     * Cancels all actions and closes the stage.
     *
     * @param event
     */
    @FXML
    private void cancelScene(ActionEvent event) {
        closeScene();
    }

   /**
     * This methods runs when the AddEditProject FXML is opened by the "create" button. It takes the update statistics thread and stores it as an instance variable.
     *
     * @param thread the Thread returned by method updateView in the MainAdminViewController
     */
    public void setUpdateThread(Thread thread) {
        this.updateThread = thread;
        activeUsersCBox.setItems(appModel.loadActiveEmployees());
        customerComboBox.setItems(customerModel.getAllCustomers());

    }

    /**
     * This methods runs when the AddEditProject FXML is opened by the "edit" button. It takes the selected project and update thread and stores them as instance variables.
     * @param thread The Thread returned by method updateView in the MainAdminViewController
     * @param project The project to be updated 
     */
    public void setProject(Thread thread, Project project) {
        this.updateThread = thread;
        this.projectToUpdate = project;

        registerButton.setVisible(false);
        updateButton.setVisible(true);

        customerComboBox.setItems(customerModel.getAllCustomers());
        activeUsersList.setItems(appModel.loadEmployeesAssignedToProject(project.getId()));

        nameField.setText(projectToUpdate.getName());
        customerComboBox.getSelectionModel().select(project.getCustomer());
        rateField.setText(projectToUpdate.getRate() + "");

        updateCheckBox();

    }

    /**
     * Cancels all actions and closes the scene.
     */
    private void closeScene() {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Runs method 'validateInput' when new information is entered.
     * @param event 
     */
    @FXML
    private void validateCustomer(ActionEvent event) {
        validateInput();
    }

}
