/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui;

import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Task;
import trip.bll.EmployeeManager;
import trip.bll.ProjectManager;

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
     * If correct username and password is entered, the stored person will be returned.
     *
     * @param username the username of the account
     * @param password the password of the account
     * @return
     */
    public Employee validateEmployee(String username, String password) {
        return employeeManager.validateEmployee(username, password);
    }

    public ObservableList<Employee> loadUsers() {
        return employeeManager.loadEmployees();
    }
    
    public void createUser(Employee employee, String password)
    {
        employeeManager.createUser(employee, password);
    }
}
