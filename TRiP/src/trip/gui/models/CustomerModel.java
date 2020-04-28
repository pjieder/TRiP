/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import javafx.collections.ObservableList;
import trip.be.Customer;
import trip.bll.CustomerManager;

/**
 *
 * @author Jacob
 */
public class CustomerModel
{
    private final CustomerManager customerManager;

    public CustomerModel()
    {
        customerManager = new CustomerManager();
    }
    
    public ObservableList<Customer> getAllCustomers()
    {
        return customerManager.getAllCustomers();
    }
    
    public void createCustomer(Customer customer)
    {
        customerManager.createCustomer(customer);
    }
    
    public void updateCustomer(Customer customer)
    {
        customerManager.updateCustomer(customer);
    }
    
    public void deleteCustomer(Customer customer)
    {
        customerManager.deleteCustomer(customer);
    }
}
