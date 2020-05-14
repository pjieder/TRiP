/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import java.sql.SQLException;
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
     * @throws java.sql.SQLException
     */
    public void createCustomer(Customer customer) throws SQLException;

    /**
     * Adds the customer to the project.
     *
     * @param customer The customer to be added.
     * @param project The project to be added.
     * @throws java.sql.SQLException
     */
    public void addCustomerToProject(Customer customer, Project project) throws SQLException;

    /**
     * Loads all customers stored in the database.
     *
     * @return Returns an observablelist containing all the stored customers.
     * @throws java.sql.SQLException
     */
    public ObservableList<Customer> getAllCustomers() throws SQLException;

    /**
     * Returns the customer for the specified project.
     *
     * @param projectID The project ID of the specified project.
     * @return The customer on the project.
     * @throws java.sql.SQLException
     */
    public Customer getCustomerForProject(int projectID) throws SQLException;

    /**
     * Updates the specified customer in the database.
     *
     * @param customer The customer that will update the previous customer with the same ID.
     * @throws java.sql.SQLException
     */
    public void updateCustomer(Customer customer) throws SQLException;

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer The customer to be deleted.
     * @throws java.sql.SQLException
     */
    public void deleteCustomer(Customer customer) throws SQLException;

    /**
     * Deletes the customer from the specified project.
     *
     * @param projectID The ID of the project that the costumer should be removed from.
     * @throws java.sql.SQLException
     */
    public void removeCustomerFromProject(int projectID) throws SQLException;
    
}
