/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.dal.dbmanagers.facades.DalFacade;
import trip.dal.dbmanagers.facades.IDalFacade;

/**
 *
 * @author ander
 */
public class EmployeeManager {

    private IDalFacade dalFacade;

    public EmployeeManager() {
        dalFacade = new DalFacade();
    }

    /**
     * Returns the person found by the entered username and password.
     *
     * @param username The username of the person
     * @param password The password of the person
     * @return The person found based on the username and password
     */
    public Employee validateEmployee(String username, String password) {
        return dalFacade.login(username, password);
    }

    public void createEmployee(Employee employee, String password) {
        dalFacade.createEmployee(employee, password);
    }

    public ObservableList<Employee> loadActiveEmployees() {
        return dalFacade.loadActiveEmployees();
    }

    public ObservableList<Employee> loadInactiveEmployees() {
        return dalFacade.loadInactiveEmployees();
    }

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) {
        return dalFacade.loadEmployeesAssignedToProject(projectId);
    }

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) {
        return dalFacade.loadEmployeesAssignedToProject(projectId, isActive);
    }

    public void updateEmployee(Employee employee) {
        dalFacade.updateEmployee(employee);
    }

    public void updateEmployeeActive(int employeeId, boolean active) {
        dalFacade.updateEmployeeActive(employeeId, active);
    }

    public void updatePassword(String username, String password, int id) {
        dalFacade.updatePassword(username, password, id);
    }

    public void deleteEmployee(Employee employee) {
        dalFacade.deleteEmployee(employee);
    }

    public ObservableList<Employee> searchEmployee(String employeeName, ObservableList<Employee> employeeList) {

        ObservableList<Employee> searchEmployeeList = FXCollections.observableArrayList();

        for (Employee employee : employeeList) {

            String name = employee.getfName() + " " + employee.getlName();

            if (name.toLowerCase().contains(employeeName.toLowerCase())) {

                searchEmployeeList.add(employee);
            }
        }
        return searchEmployeeList;
    }

}
