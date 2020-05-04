/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import javafx.collections.ObservableList;
import trip.be.Customer;
import trip.be.Project;

/**
 *
 * @author Jacob
 */
public interface ICustomerDBDAO {

    /**
     * Saves the newly created customer in the database.
     *
     * @param customer The customer to be saved.
     */
    public void createCustomer(Customer customer);

    /**
     * Adds the customer to the project.
     *
     * @param customer The customer to be added.
     * @param project The project to be added.
     */
    public void addCustomerToProject(Customer customer, Project project);

    /**
     * Loads all customers stored in the database.
     *
     * @return Returns an observablelist containing all the stored customers.
     */
    public ObservableList<Customer> getAllCustomers();

    /**
     * Returns the customer for the specified project.
     *
     * @param projectID The project ID of the specified project.
     * @return The customer on the project.
     */
    public Customer getCustomerForProject(int projectID);

    /**
     * Updates the specified customer in the database.
     *
     * @param customer The customer that will update the previous customer with the same ID.
     */
    public void updateCustomer(Customer customer);

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer The customer to be deleted.
     */
    public void deleteCustomer(Customer customer);

    /**
     * Deletes the customer from the specified project.
     *
     * @param projectID The ID of the project that the costumer should be removed from.
     */
    public void removeCustomerFromProject(int projectID);
}
