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
     * If correct username and password is entered, the stored person will be returned.
     *
     * @param username The username of the account
     * @param password The password of the account
     * @return
     */
    public Employee validateEmployee(String username, String password) {
        return employeeManager.validateEmployee(username, password);
    }

    /**
     * Saves the newly created employee in the database.
     *
     * @param employee The employee to be saved.
     * @param password The desired password for the newly created employee.
     */
    public void createEmployee(Employee employee, String password) {
        employeeManager.createEmployee(employee, password);
    }

    /**
     * Loads all active employees
     *
     * @return Returns an observablelist containing all active employees stored.
     */
    public ObservableList<Employee> loadActiveEmployees() {
        return employeeManager.loadActiveEmployees();
    }

    /**
     * Loads all inactive employees
     *
     * @return Returns an observablelist containing all inactive employees stored.
     */
    public ObservableList<Employee> loadInactiveEmployees() {
        return employeeManager.loadInactiveEmployees();
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @return Returns an observablelist containing all employees assigned to the specified project.
     */
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) {
        return employeeManager.loadEmployeesAssignedToProject(projectId);
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @param isActive Boolean value representing whether or not the employees should be active or inactive
     * @return Returns an observablelist containing all active or inactive employees assigned to the specified project.
     */
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) {
        return employeeManager.loadEmployeesAssignedToProject(projectId, isActive);
    }

    /**
     * Updates the specified employee in the database.
     *
     * @param employee The employee that will update the previous employee with the same ID.
     */
    public void updateEmployee(Employee employee) {
        employeeManager.updateEmployee(employee);
    }

    /**
     * Updates whether or not the specified employee is active.
     *
     * @param employeeId The ID of the employee to update.
     * @param active Boolean representing whether or not the user should be active or inactive.
     */
    public void updateEmployeeActive(int employeeId, boolean active) {
        employeeManager.updateEmployeeActive(employeeId, active);
    }

    /**
     * Updates the password of the specified user.
     *
     * @param password The new password to be hashed and stored.
     * @param id The ID of the employee to update.
     */
    public void updatePassword(String password, int id) {
        employeeManager.updatePassword(password, id);
    }

    /**
     * Deletes the specified employee from the database.
     *
     * @param employee The employee to be deleted.
     */
    public void deleteEmployee(Employee employee) {
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
