/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.facades;

import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Task;

/**
 *
 * @author ander
 */
public interface IDalFacade {

    /**
     * Returns the person found by the entered username and password.
     *
     * @param username The username of the person
     * @param password The password of the person
     * @return The person found based on the username and password
     */
    public Employee login(String username, String password);

    public ObservableList<Employee> loadEmployees();

    public void createUser(Employee employee, String password);

    public ObservableList<Task> loadTasks(int userId, int projectId);

    public ObservableList<Project> loadAllActiveProjects(int employeeId);

    public ObservableList<Project> loadUserProjects(int employeeId);
}
