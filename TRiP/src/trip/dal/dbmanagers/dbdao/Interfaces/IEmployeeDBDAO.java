/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.be.Roles;

/**
 *
 * @author ander
 */
public interface IEmployeeDBDAO {

    /**
     * Returns the ID of the user based on whether the login information given is valid or not.
     *
     * @param userName The username of the account
     * @param password The password of the account
     * @return int value representing the ID of the user. If no user is found, an ID of -1 is returned.
     */
    public int isLoginCorrect(String userName, String password);

    /**
     * Saves the newly created employee in the database.
     *
     * @param employee The employee to be saved.
     * @param password The desired password for the newly created employee.
     * @return Boolean value representing whether or not the creation was successful.
     */
    public boolean createEmployee(Employee employee, String password);

    /**
     * Hashes and salts the password entered by the user and saves it to the database.
     *
     * @param username The entered username for the employee.
     * @param password The entered password for the employee.
     * @param ID The ID of the employee.
     */
    public void createPassword(String username, String password, int ID);

    /**
     * Adds the employee to the project.
     *
     * @param employeeID The ID of the employee.
     * @param projID The ID of the project.
     */
    public void addEmployeeToProject(int employeeID, int projID);

    /**
     * Loads all active employees
     *
     * @return Returns an observablelist containing all active employees stored.
     */
    public ObservableList<Employee> loadActiveEmployees();

    /**
     * Loads all inactive employees
     *
     * @return Returns an observablelist containing all inactive employees stored.
     */
    public ObservableList<Employee> loadInactiveEmployees();

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @return Returns an observablelist containing all employees assigned to the specified project.
     */
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId);

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @param isActive Boolean value representing whether or not the employees should be active or inactive
     * @return Returns an observablelist containing all active or inactive employees assigned to the specified project.
     */
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive);

    /**
     * Returns the role of the person containing a specific ID.
     *
     * @param id The ID of the person being searched for
     * @return The role of the person entered
     */
    public Roles getRoleById(int id);

    /**
     * Updates the specified employee in the database.
     *
     * @param employee The employee that will update the previous employee with the same ID.
     * @return Boolean value representing whether or not update was successful.
     */
    public boolean updateEmployee(Employee employee);

    /**
     * Updates whether or not the specified employee is active.
     *
     * @param employeeId The ID of the employee to update.
     * @param active Boolean representing whether or not the user should be active or inactive.
     */
    public void updateEmployeeActive(int employeeId, boolean active);

    /**
     * Updates the username of the employee.
     *
     * @param username The new username wished for the employee.
     * @param ID The ID of the employee.
     */
    public void updateUsername(String username, int ID);

    /**
     * Updates the password of the specified user.
     *
     * @param password The new password to be hashed and stored.
     * @param ID The ID of the employee to update.
     */
    public void updatePassword(String password, int ID);

    /**
     * Deletes the specified employee from the database.
     *
     * @param employee The employee to be deleted.
     * @return Boolean representing whether or not the update was successful.
     */
    public boolean deleteEmployee(Employee employee);

    /**
     * Removes all employees working on the project.
     *
     * @param projID The ID of the project.
     */
    public void removeAllEmployeesFromProject(int projID);

}
