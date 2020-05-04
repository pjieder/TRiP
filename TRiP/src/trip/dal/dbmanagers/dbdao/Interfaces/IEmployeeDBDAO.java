/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.be.Project;
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
     * @param employee The employee the password should be created for.
     * @param password The entered password for the employee.
     * @param ID The ID of the employee.
     */
    public void createPassword(Employee employee, String password, int ID);

    /**
     * Adds the employee to the project.
     *
     * @param employee The employee to be added.
     * @param project The project the employee should be added to.
     */
    public void addEmployeeToProject(Employee employee, Project project);

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
     * @param employee The employee to be updated.
     * @param active Boolean representing whether or not the user should be active or inactive.
     */
    public void updateEmployeeActive(Employee employee, boolean active);

    /**
     * Updates the username of the employee.
     *
     * @param username The new username wished for the employee.
     * @param employee The employee to be updated.
     */
    public void updateUsername(String username, Employee employee);

      /**
     * Updates the password of the specified employee.
     *
     * @param password The new password to be hashed and stored.
     * @param employee The employee to be updated.
     */
    public void updatePassword(String password, Employee employee);

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
     * @param project The project all users should be removed from.
     */
    public void removeAllEmployeesFromProject(Project project);

}
