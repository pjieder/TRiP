/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Customer;
import trip.dal.dbmanagers.facades.DalFacade;
import trip.dal.dbmanagers.facades.IDalFacade;

/**
 *
 * @author Jacob
 */
public class CustomerManager {

    private IDalFacade dalFacade;

    public CustomerManager() {
        dalFacade = new DalFacade();
    }

    public void createCustomer(Customer customer) {
        dalFacade.createCustomer(customer);
    }

    public ObservableList<Customer> getAllCustomers() {
        return dalFacade.getAllCustomers();
    }

    public void updateCustomer(Customer customer) {
        dalFacade.updateCustomer(customer);
    }

    public void deleteCustomer(Customer customer) {
        dalFacade.deleteCustomer(customer);
    }

    public ObservableList<Customer> searchCustomers(String customerName, ObservableList<Customer> customerList) {

        ObservableList<Customer> searchCustomerList = FXCollections.observableArrayList();

        for (Customer customer : customerList) {

            if (customer.getName().toLowerCase().contains(customerName.toLowerCase())) {
                searchCustomerList.add(customer);
            }
        }
        return searchCustomerList;
    }
}
