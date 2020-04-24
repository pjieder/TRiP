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
import trip.be.Timer;

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

    public void createUser(Employee employee, String password);
    
    public void updateEmployee(Employee employee);
    
    public void updatePassword(String username, String password, int id);

    public int addTask(int userId, int projectId, String taskName);

    public void saveTimeForTask(Timer timer);

    public ObservableList<Employee> loadEmployees();

    public ObservableList<Task> loadTasks(int userId, int projectId);

    public ObservableList<Project> loadAllActiveProjects(int employeeId);

    public ObservableList<Project> loadUserProjects(int employeeId);

}
