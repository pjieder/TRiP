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

    /**
     * Saves the newly created customer in the database.
     *
     * @param customer The customer to be saved.
     */
    public void createCustomer(Customer customer) {
        dalFacade.createCustomer(customer);
    }

    /**
     * Loads all customers stored in the database.
     *
     * @return Returns an observablelist containing all the stored customers.
     */
    public ObservableList<Customer> getAllCustomers() {
        return dalFacade.getAllCustomers();
    }

    /**
     * Updates the specified customer in the database.
     *
     * @param customer The customer that will update the previous customer with the same ID.
     */
    public void updateCustomer(Customer customer) {
        dalFacade.updateCustomer(customer);
    }

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer The customer to be deleted.
     */
    public void deleteCustomer(Customer customer) {
        dalFacade.deleteCustomer(customer);
    }

    /**
     * Searches for customers matching the search term.
     *
     * @param customerName The name of the customer being searched for.
     * @param customerList The List of all customers the search should be based on.
     * @return An observablelist of all the customers found matching the search term.
     */
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
