/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

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

    public void createUser(Employee employee, String password) {
        dalFacade.createUser(employee, password);
    }

    public void updateEmployee(Employee employee) {
        dalFacade.updateEmployee(employee);
    }
    
    public void updatePassword(String username, String password, int id) {
        dalFacade.updatePassword(username, password, id);
    }
    
    public ObservableList<Employee> loadEmployees() {
        return dalFacade.loadEmployees();
    }
    
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId)
    {
        return dalFacade.loadEmployeesAssignedToProject(projectId);
    }

}
