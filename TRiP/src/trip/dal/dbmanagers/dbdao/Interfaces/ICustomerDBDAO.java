/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import javafx.collections.ObservableList;
import trip.be.Customer;

/**
 *
 * @author Jacob
 */
public interface ICustomerDBDAO {

    public void createCustomer(Customer customer);

    public void addCustomerToProject(int customerID, int projectID);

    public ObservableList<Customer> getAllCustomers();

    public Customer getCustomerForProject(int projectID);

    public void updateCustomer(Customer customer);

    public void deleteCustomer(Customer customer);

    public void removeCustomerFromProject(int projectID);
}
