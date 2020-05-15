/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.bll.EmployeeManager;

/**
 *
 * @author ander
 */
public class EmployeeModel {

    private final EmployeeManager employeeManager;

    public EmployeeModel() {
        employeeManager = new EmployeeManager();
    }

    /**
     * Returns the employee found by the entered username and password.
     *
     * @param username The username of the employee
     * @param password The password of the employee
     * @return The employee found based on the username and password
     * @throws java.sql.SQLException
     */
    public Employee validateEmployee(String username, String password) throws SQLException{
        return employeeManager.validateEmployee(username, password);
    }

    /**
     * Saves the newly created employee in the database.
     *
     * @param employee The employee to be saved.
     * @param password The desired password for the newly created employee.
     * @throws java.sql.SQLException
     * @return A boolean value representing whether or not the user is created.
     */
    public boolean createEmployee(Employee employee, String password) throws SQLException{
       return employeeManager.createEmployee(employee, password);
    }

    /**
     * Loads all active employees
     *
     * @return Returns an observablelist containing all active employees stored.
     * @throws java.sql.SQLException
     */
    public ObservableList<Employee> loadActiveEmployees() throws SQLException{
        return employeeManager.loadActiveEmployees();
    }

    /**
     * Loads all inactive employees
     *
     * @return Returns an observablelist containing all inactive employees stored.
     * @throws java.sql.SQLException
     */
    public ObservableList<Employee> loadInactiveEmployees() throws SQLException{
        return employeeManager.loadInactiveEmployees();
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @return Returns an observablelist containing all employees assigned to the specified project.
     * @throws java.sql.SQLException
     */
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) throws SQLException{
        return employeeManager.loadEmployeesAssignedToProject(projectId);
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @param isActive Boolean value representing whether or not the employees should be active or inactive
     * @return Returns an observablelist containing all active or inactive employees assigned to the specified project.
     * @throws java.sql.SQLException
     */
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) throws SQLException{
        return employeeManager.loadEmployeesAssignedToProject(projectId, isActive);
    }

    /**
     * Updates the specified employee in the database.
     *
     * @param employee The employee that will update the previous employee with the same ID.
     * @throws java.sql.SQLException
     */
    public void updateEmployee(Employee employee) throws SQLException{
        employeeManager.updateEmployee(employee);
    }

    /**
     * Updates whether or not the specified employee is active.
     *
     * @param employee The employee to be updated.
     * @param active Boolean representing whether or not the user should be active or inactive.
     * @throws java.sql.SQLException
     */
    public void updateEmployeeActive(Employee employee, boolean active) throws SQLException{
        employeeManager.updateEmployeeActive(employee, active);
    }

    /**
     * Updates the password of the specified employee.
     *
     * @param password The new password to be hashed and stored.
     * @param employee The employee to be updated.
     * @throws java.sql.SQLException
     */
    public void updatePassword(String password, Employee employee) throws SQLException{
        employeeManager.updatePassword(password, employee);
    }

    /**
     * Deletes the specified employee from the database.
     *
     * @param employee The employee to be deleted.
     * @throws java.sql.SQLException
     */
    public void deleteEmployee(Employee employee) throws SQLException{
        employeeManager.deleteEmployee(employee);
    }

    /**
     * Searches for employees matching the search term.
     *
     * @param employeeName The name of the employee being searched for.
     * @param employeeList The list of all employees the search should be based on.
     * @return An observablelist of all the employees found matching the search term.
     */
    public ObservableList<Employee> searchEmployee(String employeeName, ObservableList<Employee> employeeList) {
        return employeeManager.searchEmployee(employeeName, employeeList);
    }
    
}
