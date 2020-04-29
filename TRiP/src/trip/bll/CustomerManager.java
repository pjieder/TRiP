/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Customer;
import trip.be.Project;
import trip.dal.dbmanagers.dbdao.CustomerDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.ICustomerDBDAO;

/**
 *
 * @author Jacob
 */
public class CustomerManager
{

    private ICustomerDBDAO customerDao;

    public CustomerManager()
    {
        customerDao = new CustomerDBDAO();
    }

    public ObservableList<Customer> getAllCustomers()
    {
        return customerDao.getAllCustomers();
    }

    public void createCustomer(Customer customer)
    {
        customerDao.createCustomer(customer);
    }

    public void updateCustomer(Customer customer)
    {
        customerDao.updateCustomer(customer);
    }

    public void deleteCustomer(Customer customer)
    {
        customerDao.deleteCustomer(customer);
    }

    public ObservableList<Customer> searchCustomers(String customerName, ObservableList<Customer> customerList)
    {

        ObservableList<Customer> searchCustomerList = FXCollections.observableArrayList();

        for (Customer customer : customerList)
        {

            if (customer.getName().toLowerCase().contains(customerName.toLowerCase()))
            {
                searchCustomerList.add(customer);
            }
        }
        return searchCustomerList;
    }
}
