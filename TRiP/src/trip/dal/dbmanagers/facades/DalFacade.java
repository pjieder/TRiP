/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.facades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import trip.be.Customer;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Task;
import trip.be.CountedTime;
import trip.dal.dbmanagers.dbdao.CustomerDBDAO;
import trip.dal.dbmanagers.dbdao.EmployeeDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.ICustomerDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.IEmployeeDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.IProjectDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.ITaskDBDAO;
import trip.dal.dbmanagers.dbdao.ProjectDBDAO;
import trip.dal.dbmanagers.dbdao.TaskDBDAO;
import java.sql.SQLException;

/**
 *
 * @author ander
 */
public class DalFacade implements IDalFacade {

    private IEmployeeDBDAO employeeDBDAO;
    private IProjectDBDAO projectManager;
    private ITaskDBDAO taskManager;
    private ICustomerDBDAO customerManager;

    public DalFacade() {

        employeeDBDAO = new EmployeeDBDAO();
        projectManager = new ProjectDBDAO();
        taskManager = new TaskDBDAO();
        customerManager = new CustomerDBDAO();
    }

    /**
     * Returns the employee found by the entered username and password.
     *
     * @param username The username of the employee
     * @param password The password of the employee
     * @return The employee found based on the username and password
     * @throws java.sql.SQLException
     */
    @Override
    public Employee login(String username, String password) throws SQLException{

        Employee employee = null;
        int employeeId = employeeDBDAO.isLoginCorrect(username, password);

        if (employeeId != -1) {
            
            return employeeDBDAO.getEmployeeById(employeeId);
        }
        return employee;
    }

    /**
     * Saves the newly created employee in the database.
     *
     * @param employee The employee to be saved.
     * @param password The desired password for the newly created employee.
     * @return A boolean value representing whether or not the user is created.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean createEmployee(Employee employee, String password) throws SQLException{
        
        if (employeeDBDAO.checkExistingEmployee(employee.getEmail())){return false;}
        return employeeDBDAO.createEmployee(employee, password);
    }

    /**
     * Loads all active employees
     *
     * @return Returns an observablelist containing all active employees stored.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadActiveEmployees() throws SQLException{
        return employeeDBDAO.loadActiveEmployees();
    }

    /**
     * Loads all inactive employees
     *
     * @return Returns an observablelist containing all inactive employees stored.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadInactiveEmployees() throws SQLException{
        return employeeDBDAO.loadInactiveEmployees();
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @return Returns an observablelist containing all employees assigned to the specified project.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) throws SQLException{
        return employeeDBDAO.loadEmployeesAssignedToProject(projectId);
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @param isActive Boolean value representing whether or not the employees should be active or inactive
     * @return Returns an observablelist containing all active or inactive employees assigned to the specified project.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) throws SQLException{
        return employeeDBDAO.loadEmployeesAssignedToProject(projectId, isActive);
    }

    /**
     * Updates the specified employee in the database.
     *
     * @param employee The employee that will update the previous employee with the same ID.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateEmployee(Employee employee) throws SQLException{
        employeeDBDAO.updateEmployee(employee);
        employeeDBDAO.updateUsername(employee.getEmail(), employee);
    }

    /**
     * Updates whether or not the specified employee is active.
     *
     * @param employee The employee to be updated.
     * @param active Boolean representing whether or not the user should be active or inactive.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateEmployeeActive(Employee employee, boolean active) throws SQLException{
        employeeDBDAO.updateEmployeeActive(employee, active);
    }

    /**
     * Updates the password of the specified employee.
     *
     * @param password The new password to be hashed and stored.
     * @param employee The employee to be updated.
     * @throws java.sql.SQLException
     */
    @Override
    public void updatePassword(String password, Employee employee) throws SQLException{
        employeeDBDAO.updatePassword(password, employee);
    }

    /**
     * Deletes the specified employee from the database.
     *
     * @param employee The employee to be deleted.
     * @throws java.sql.SQLException
     */
    @Override
    public void deleteEmployee(Employee employee) throws SQLException{
        employeeDBDAO.deleteEmployee(employee);
    }

    /**
     * Saves the newly created project in the database together with the employees assigned to the project.
     *
     * @param project The project to be saved.
     * @param allEmployees The employees that should be registered to the project.
     * @throws java.sql.SQLException
     */
    @Override
    public void createProject(Project project, List<Employee> allEmployees) throws SQLException{
        projectManager.createProject(project);
        customerManager.addCustomerToProject(project.getCustomer(), project);
        for (Employee employee : allEmployees) {
            employeeDBDAO.addEmployeeToProject(employee, project);
        }
    }

    /**
     * Loads all saved projects in the database.
     *
     * @return An observablelist containing all projects stored.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Project> getAllProjects() throws SQLException{
        return projectManager.loadAllProjects();
    }

    /**
     * Loads all active projects stored in the database.
     *
     * @return An observablelist containing all active projects stored.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Project> loadAllActiveProjects() throws SQLException{
        ObservableList<Project> allActiveProjects = projectManager.loadAllActiveProjects();
        for (Project project : allActiveProjects) {
            project.setTotalTime(projectManager.loadTotalProjectTime(project.getId()));
            project.setCustomer(customerManager.getCustomerForProject(project.getId()));
        }
        return allActiveProjects;
    }

    /**
     * Loads all inactiev projects stored in the database.
     *
     * @return An observablelist containing all the inactive projects stored.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Project> loadAllInactiveProjects() throws SQLException{
        ObservableList<Project> allActiveProjects = projectManager.loadAllInactiveProjects();
        for (Project project : allActiveProjects) {
            project.setTotalTime(projectManager.loadTotalProjectTime(project.getId()));
            project.setCustomer(customerManager.getCustomerForProject(project.getId()));
        }
        return allActiveProjects;
    }

    /**
     * Loads all stored projects assigned to the specified employee.
     *
     * @param employeeId The ID of the employee.
     * @return Returns an observablelist containing all projects assigned to the specified employee.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Project> loadEmployeeProjects(int employeeId) throws SQLException{
        ObservableList<Project> allUserProjects = projectManager.loadEmployeeProjects(employeeId);
        for (Project project : allUserProjects) {
            project.setTotalTime(projectManager.loadProjectTime(employeeId, project.getId()));
        }
        return allUserProjects;
    }

    /**
     * Loads the overall time used on the specified project on the specified days
     *
     * @param projectID The ID of the project searching for.
     * @param dates A list of all dates that the total time is wanted for.
     * @return A list of all time used each day of the specified days in seconds.
     * @throws java.sql.SQLException
     */
    @Override
    public List<Integer> loadTimeForDates(int projectID, List<LocalDate> dates) throws SQLException{

        List<Integer> dateTime = new ArrayList();

        if (projectID == 0) {
            for (LocalDate date : dates) {

                dateTime.add(projectManager.loadAllProjectTimeForDay(date));
            }
        } else {
            for (LocalDate date : dates) {

                dateTime.add(projectManager.loadProjectTimeForDay(projectID, date));
            }
        }
        return dateTime;
    }

    /**
     * Loads all projects having been worked on between the specified dates by the specified employee.
     *
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @param employeeID The ID of the specified employee.
     * @return A list containing all projects having been worked on by the specified employee between the two dates.
     * @throws java.sql.SQLException
     */
    @Override
    public List<Project> loadWorkedOnProjectsBetweenDates(LocalDate startDate, LocalDate endDate, int employeeID) throws SQLException{
        return projectManager.loadWorkedOnProjectsBetweenDates(startDate, endDate, employeeID);
    }

    /**
     * Calculates the chart series for the bargraph representing the total amount of time individual users have been working and on what tasks between the specified days.
     *
     * @param startDate The startdate of the timeframe.
     * @param endDate The enddate of the timeframe.
     * @param employeeID The Id of the employee that the series is based upon.
     * @return A series containing all the stored data to be displayed.
     * @throws java.sql.SQLException
     */
    @Override
    public XYChart.Series loadTimeForUsersProjects(LocalDate startDate, LocalDate endDate, int employeeID) throws SQLException{
        return projectManager.loadTimeForUsersProjects(startDate, endDate, employeeID);
    }

    /**
     * Loads the total amount of time having been registered on the specified project between a specified timespan.
     *
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @param isBillable Boolean value representing whether or not the time is billable.
     * @return An int value representing the total amount of time having been used in seconds.
     * @throws java.sql.SQLException
     */
    @Override
    public int loadAllProjectTimeBetweenDates(int projectID, LocalDate startDate, LocalDate endDate, boolean isBillable) throws SQLException{
        return projectManager.loadAllProjectTimeBetweenDates(projectID, startDate, endDate, isBillable);
    }

    /**
     * Loads the total amount of time having been registered on the specifiec project by the specified employee between the timespan.
     *
     * @param employeeID The ID of the specified employee .
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @param isBillable Boolean value representing whether or not the time is billable.
     * @return An int value representing the total amount of time the specified employe have been working on the project in seconds.
     * @throws java.sql.SQLException
     */
    @Override
    public int loadAllEmployeeProjectTimeBetweenDates(int employeeID, int projectID, LocalDate startDate, LocalDate endDate, boolean isBillable) throws SQLException{
        return projectManager.loadAllEmployeeProjectTimeBetweenDates(employeeID, projectID, startDate, endDate, isBillable);
    }

    /**
     * Updates the specified project in the database.
     *
     * @param project The project that will update the previous project with the same ID.
     * @param allEmployees The employees that should be registered to the project.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateProject(Project project, List<Employee> allEmployees) throws SQLException{
        projectManager.updateProject(project);
        customerManager.removeCustomerFromProject(project.getId());
        customerManager.addCustomerToProject(project.getCustomer(), project);
        employeeDBDAO.removeAllEmployeesFromProject(project);

        for (Employee employee : allEmployees) {
            employeeDBDAO.addEmployeeToProject(employee, project);
        }
    }
    
    /**
     * Updates whether or not the specified project is active.
     *
     * @param project The project to be updated.
     * @param active Boolean representing whether or not the project should be active or inactive.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateProjectActive(Project project, boolean active) throws SQLException{
        projectManager.updateProjectActive(project, active);
    }

    /**
     * Deletes the specified project from the database.
     *
     * @param project The project to be deleted.
     * @throws java.sql.SQLException
     */
    @Override
    public void deleteProject(Project project) throws SQLException{
        projectManager.deleteProject(project);
    }

    /**
     * Saves the newly created task in the database.
     *
     * @param userId The ID of the user working on the task.
     * @param projectId The ID of the project that the task is associated to.
     * @param taskName The name of the task.
     * @return The newly created task.
     * @throws java.sql.SQLException
     */
    @Override
    public Task addTask(int userId, int projectId, String taskName) throws SQLException{
        return taskManager.addTask(userId, projectId, taskName);
    }

    /**
     * Loads all tasks stored in the database by the specified employee on the specified project.
     *
     * @param employeeId The ID of the specified employee.
     * @param projectId The ID of the specified project.
     * @return An observablelist containing all stored tasks for the specified user and project.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Task> loadTasks(int employeeId, int projectId) throws SQLException{

        ObservableList<Task> tasks = taskManager.loadTasks(employeeId, projectId);

        for (Task task : tasks) {

            task.setCountedTime(taskManager.loadTimeForTask(task.getId()));
            task.setTotalTime(taskManager.getCountedTime(task.getId()));
        }
        return tasks;
    }
    
    /**
     * Loads all unique tasks having been worked on between two specified dates within the specified project.
     * @param projectID The id of the project searching tasks for.
     * @param startDate The startdate of the time span.
     * @param endDate The enddate of the time span.
     * @return An observablelist containing all the stored tasks searched for.
     * @throws SQLException 
     */
    @Override
    public ObservableList<Task> loadAllUniqueTasksDates(int projectID, LocalDate startDate, LocalDate endDate) throws SQLException
    {
        return taskManager.loadAllUniqueTasksDates(projectID, startDate, endDate);
    }

    /**
     * Updates the specified task in the database.
     *
     * @param task The task that will update the previous task with the same ID.
     * @return A boolean value representing whether or not the task was updated.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updateTask(Task task) throws SQLException{
        return taskManager.updateTask(task);
    }

    /**
     * Deletes the specified task from the database.
     *
     * @param task The task to be deleted.
     * @return A boolean value representing whether or not the task was deleted.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean deleteTask(Task task) throws SQLException{
        return taskManager.deleteTask(task);
    }

    /**
     * Saves the time having been worked on the task in the database.
     *
     * @param task The task being worked on.
     * @param time The total amount of time having been worked on the task in seconds.
     * @param startTime The starttime of when the work began.
     * @param stopTime The endtime of when the work ended.
     * @throws java.sql.SQLException
     */
    @Override
    public void saveTimeForTask(Task task, int time, Date startTime, Date stopTime) throws SQLException{
        taskManager.addTimeForTask(task, time, startTime, stopTime);
    }

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param countedTime The counted time that will update the previous counred time with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updateTimeForTask(CountedTime countedTime) throws SQLException{
        return taskManager.updateTimeForTask(countedTime);
    }

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param countedTime The counted time to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean deleteTimeForTask(CountedTime countedTime) throws SQLException{
        return taskManager.deleteTimeForTask(countedTime);
    }

    /**
     * Saves the newly created customer in the database.
     *
     * @param customer The customer to be saved.
     * @throws java.sql.SQLException
     */
    @Override
    public void createCustomer(Customer customer) throws SQLException{
        customerManager.createCustomer(customer);
    }

    /**
     * Loads all customers stored in the database.
     *
     * @return Returns an observablelist containing all the stored customers.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException{
        return customerManager.getAllCustomers();
    }

    /**
     * Updates the specified customer in the database.
     *
     * @param customer The customer that will update the previous customer with the same ID.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateCustomer(Customer customer) throws SQLException{
        customerManager.updateCustomer(customer);
    }

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer The customer to be deleted.
     * @throws java.sql.SQLException
     */
    @Override
    public void deleteCustomer(Customer customer) throws SQLException{
        customerManager.deleteCustomer(customer);
    }

}
