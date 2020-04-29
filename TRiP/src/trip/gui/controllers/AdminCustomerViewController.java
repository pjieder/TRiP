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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import trip.be.Customer;
import trip.be.Project;
import trip.gui.AppModel;
import trip.gui.models.CustomerModel;

/**
 * FXML Controller class
 *
 * @author Jacob
 */
public class AdminCustomerViewController implements Initializable
{

    private CustomerModel customerModel = new CustomerModel();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TextField searchBar;
    @FXML
    private JFXButton deleteButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        nameColumn.setCellValueFactory((data) ->
        {
            Customer customer = data.getValue();
            return new SimpleStringProperty(customer.getName());
        });

        phoneNumberColumn.setCellValueFactory((data) ->
        {
            Customer customer = data.getValue();
            return new SimpleStringProperty(customer.getPhoneNumber());
        });

        emailColumn.setCellValueFactory((data) ->
        {
            Customer customer = data.getValue();
            return new SimpleStringProperty(customer.getEmail());
        });
    }

    public void loadAllCustomers()
    {
        customers = customerModel.getAllCustomers();
        customerTable.setItems(customers);
    }

    @FXML
    private void openUsers(ActionEvent event)
    {
    }

    @FXML
    private void openProjectMenu(MouseEvent event)
    {
    }

    @FXML
    private void log_out(MouseEvent event)
    {
    }

    @FXML
    private void createCustomer(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AppModel.class.getResource("views/AddEditCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        AddEditCustomerController controller = fxmlLoader.getController();
        controller.setUpdateThread(updateView());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void editCustomer(ActionEvent event) throws IOException
    {
        if (!customerTable.getSelectionModel().isEmpty())
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(AppModel.class.getResource("views/AddEditCustomer.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            AddEditCustomerController controller = fxmlLoader.getController();
            controller.setCustomer(customerTable.getSelectionModel().getSelectedItem(), updateView());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void deleteCustomer(ActionEvent event)
    {
        if (!customerTable.getSelectionModel().isEmpty())
        {

            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Confirm delete");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete " + selectedCustomer.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                customerTable.getItems().remove(selectedCustomer);
                customerModel.deleteCustomer(selectedCustomer);
            } else
            {
                alert.close();
            }

        }
    }

    private Thread updateView()
    {
        Thread updateThread = new Thread(() ->
        {
            Platform.runLater(() ->
            {
                customers = customerModel.getAllCustomers();
                customerTable.setItems(customers);
                customerTable.refresh();
                search();
            });
        });
        return updateThread;
    }

    @FXML
    private void customerSearch(KeyEvent event)
    {
        search();
    }

    public void search()
    {
        String customerName = searchBar.getText();

        if (customerName.equalsIgnoreCase(""))
        {
            customerTable.setItems(customers);
        } else
        {
            customerTable.setItems(customerModel.searchCustomers(customerName, customers));
        }
    }

}
