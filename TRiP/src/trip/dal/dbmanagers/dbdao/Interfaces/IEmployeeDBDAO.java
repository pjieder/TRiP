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
     * adds employee to Employees Table in SQL
     *
     * @param employee The employee to add
     * @param password the password wanted stored
     * @return
     */
    public boolean createEmployee(Employee employee, String password);

    public void createPassword(String userName, String password, int ID);

    public void addEmployeeToProject(int employeeID, int projID);

    public ObservableList<Employee> loadActiveEmployees();

    public ObservableList<Employee> loadInactiveEmployees();

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId);

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive);

    /**
     * Returns the role of the person containing a specific ID.
     *
     * @param id The ID of the person being searched for
     * @return The role of the person entered
     */
    public Roles getRoleById(int id);

    public boolean updateEmployee(Employee employee);

    public void updateEmployeeActive(int employeeId, boolean active);
    
    public void updateUsername(String username, int ID);

    public void updatePassword(String password, int ID);

    public boolean deleteEmployee(Employee employee);

    public void removeAllEmployeesFromProject(int projID);

}
