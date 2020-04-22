/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui;

import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.bll.EmployeeManager;

/**
 *
 * @author ander
 */
public class AppModel {

    private final EmployeeManager employeeManager;

    public AppModel() {
        employeeManager = new EmployeeManager();
    }
    
    /**
     * If correct username and password is entered,
     * the stored person will be returned.
     * @param username the username of the account
     * @param password the password of the account
     * @return 
     */
    public Employee validateEmployee(String username, String password) {
        return employeeManager.validateEmployee(username, password);
    }

        public ObservableList<Employee> loadUsers()
    {
        return employeeManager.loadEmployees();
    }
    
    
}
