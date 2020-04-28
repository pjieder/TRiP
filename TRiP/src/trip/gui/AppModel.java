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
     * @param username the username of the account
     * @param password the password of the account
     * @return
     */
    public Employee validateEmployee(String username, String password) {
        return employeeManager.validateEmployee(username, password);
    }

    public void createUser(Employee employee, String password) {
        employeeManager.createUser(employee, password);
    }

    public void deleteEmployee(Employee employee) {
        employeeManager.deleteEmployee(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeManager.updateEmployee(employee);
    }

    public void updatePassword(String username, String password, int id) {
        employeeManager.updatePassword(username, password, id);
    }

    public ObservableList<Employee> loadActiveUsers() {
        return employeeManager.loadActiveUsers();
    }

    public void updateEmployeeActive(int employeeId, boolean active) {
        employeeManager.updateEmployeeActive(employeeId, active);
    }

    public ObservableList<Employee> loadInactiveUsers() {
        return employeeManager.loadInactiveUsers();
    }

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) {
        return employeeManager.loadEmployeesAssignedToProject(projectId);
    }

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) {
        return employeeManager.loadEmployeesAssignedToProject(projectId, isActive);
    }

    public ObservableList<Employee> searchEmployee(String employeeName, ObservableList<Employee> employeeList) {
        return employeeManager.searchEmployee(employeeName, employeeList);
    }
}
