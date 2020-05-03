/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.facades;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import trip.be.Customer;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Task;
import trip.be.TaskTime;

/**
 *
 * @author ander
 */
public interface IDalFacade {

    /**
     * Returns the employee found by the entered username and password.
     *
     * @param username The username of the employee
     * @param password The password of the employee
     * @return The employee found based on the username and password
     */
    public Employee login(String username, String password);

    /**
     * Saves the newly created employee in the database.
     *
     * @param employee The employee to be saved.
     * @param password The desired password for the newly created employee.
     */
    public void createEmployee(Employee employee, String password);

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
     * Updates the specified employee in the database.
     *
     * @param employee The employee that will update the previous employee with the same ID.
     */
    public void updateEmployee(Employee employee);

    /**
     * Updates whether or not the specified employee is active.
     *
     * @param employeeId The ID of the employee to update.
     * @param active Boolean representing whether or not the user should be active or inactive.
     */
    public void updateEmployeeActive(int employeeId, boolean active);

    /**
     * Updates the password of the specified user.
     *
     * @param password The new password to be hashed and stored.
     * @param id The ID of the employee to update.
     */
    public void updatePassword(String password, int id);

    /**
     * Deletes the specified employee from the database.
     *
     * @param employee The employee to be deleted.
     */
    public void deleteEmployee(Employee employee);

    /**
     * Saves the newly created project in the database together with the employees assigned to the project.
     *
     * @param project The project to be saved.
     * @param allEmployees The employees that should be registered to the project.
     */
    public void createProject(Project project, List<Employee> allEmployees);

    /**
     * Loads all saved projects in the database.
     *
     * @return An observablelist containing all projects stored.
     */
    public ObservableList<Project> getAllProjects();

    /**
     * Loads all active projects stored in the database.
     *
     * @return An observablelist containing all active projects stored.
     */
    public ObservableList<Project> loadAllActiveProjects();

    /**
     * Loads all inactiev projects stored in the database.
     *
     * @return An observablelist containing all the inactive projects stored.
     */
    public ObservableList<Project> loadAllInactiveProjects();

    /**
     * Loads all stored projects assigned to the specified employee.
     *
     * @param employeeId The ID of the employee.
     * @return Returns an observablelist containing all projects assigned to the specified employee.
     */
    public ObservableList<Project> loadEmployeeProjects(int employeeId);

    /**
     * Loads the overall time used on the specified project on the specified days
     *
     * @param projectID The ID of the project searching for.
     * @param dates A list of all dates that the total time is wanted for.
     * @return A list of all time used each day of the specified days in seconds.
     */
    public List<Integer> loadTimeForDates(int projectID, List<LocalDate> dates);

    /**
     * Loads all projects having been worked on between the specified dates by the specified employee.
     *
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @param employeeID The ID of the specified employee.
     * @return A list containing all projects having been worked on by the specified employee between the two dates.
     */
    public List<Project> loadWorkedOnProjectsBetweenDates(LocalDate startDate, LocalDate endDate, int employeeID);

    /**
     * Calculates the chart series for the bargraph representing the total amount of time individual users have been working and on what tasks between the specified days.
     *
     * @param startDate The startdate of the timeframe.
     * @param endDate The enddate of the timeframe.
     * @param employeeID The Id of the employee that the series is based upon.
     * @return A series containing all the stored data to be displayed.
     */
    public XYChart.Series loadTimeForUsersProjects(LocalDate startDate, LocalDate endDate, int employeeID);

    /**
     * Loads the total amount of time having been registered on the specified project between a specified timespan.
     *
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @return An int value representing the total amount of time having been used in seconds.
     */
    public int loadAllProjectTimeBetweenDates(int projectID, LocalDate startDate, LocalDate endDate);

    /**
     * Loads the total amount of time having been registered on the specifiec project by the specified employee between the timespan.
     *
     * @param employeeID The ID of the specified employee .
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @return An int value representing the total amount of time the specified employe have been working on the project in seconds.
     */
    public int loadAllEmployeeProjectTimeBetweenDates(int employeeID, int projectID, LocalDate startDate, LocalDate endDate);

    /**
     * Updates the specified project in the database.
     *
     * @param project The project that will update the previous project with the same ID.
     * @param allEmployees The employees that should be registered to the project.
     */
    public void updateProject(Project project, List<Employee> allEmployees);

    /**
     * Deletes the specified project from the database.
     *
     * @param project The project to be deleted.
     */
    public void deleteProject(Project project);

    /**
     * Saves the newly created task in the database.
     *
     * @param userId The ID of the user working on the task.
     * @param projectId The ID of the project that the task is associated to.
     * @param taskName The name of the task.
     * @return The ID of the newly created task.
     */
    public int addTask(int userId, int projectId, String taskName);

    /**
     * Loads all tasks stored in the database by the specified employee on the specified project.
     *
     * @param employeeId The ID of the specified employee.
     * @param projectId The ID of the specified project.
     * @return An observablelist containing all stored tasks for the specified user and project.
     */
    public ObservableList<Task> loadTasks(int employeeId, int projectId);

    /**
     * Updates the specified task in the database.
     *
     * @param task The task that will update the previous task with the same ID.
     * @return A boolean value representing whether or not the task was updated.
     */
    public boolean updateTask(Task task);

    /**
     * Deletes the specified task from the database.
     *
     * @param taskId The ID of the task to be deleted.
     * @return A boolean value representing whether or not the task was deleted.
     */
    public boolean deleteTask(int taskId);

    /**
     * Saves the time having been worked on the task in the database.
     *
     * @param taskId The ID of the task being worked on.
     * @param time The total amount of time having been worked on the task in seconds.
     * @param startTime The starttime of when the work began.
     * @param stopTime The endtime of when the work ended.
     */
    public void saveTimeForTask(int taskId, int time, Date startTime, Date stopTime);

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param taskTime The taskTime that will update the previous taskTime with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     */
    public boolean UpdateTimeForTask(TaskTime taskTime);

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param taskTime The taskTime to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     */
    public boolean DeleteTimeForTask(TaskTime taskTime);

    /**
     * Saves the newly created customer in the database.
     *
     * @param customer The customer to be saved.
     */
    public void createCustomer(Customer customer);

    /**
     * Loads all customers stored in the database.
     *
     * @return Returns an observablelist containing all the stored customers.
     */
    public ObservableList<Customer> getAllCustomers();

    /**
     * Updates the specified customer in the database.
     *
     * @param customer The customer that will update the previous customer with the same ID.
     */
    public void updateCustomer(Customer customer);

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer The customer to be deleted.
     */
    public void deleteCustomer(Customer customer);

}
