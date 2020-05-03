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
     * Returns the person found by the entered username and password.
     *
     * @param username The username of the person
     * @param password The password of the person
     * @return The person found based on the username and password
     */
    public Employee login(String username, String password);

    public void createEmployee(Employee employee, String password);

    public ObservableList<Employee> loadActiveEmployees();

    public ObservableList<Employee> loadInactiveEmployees();

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId);

    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive);

    public void updateEmployee(Employee employee);

    public void updateEmployeeActive(int employeeId, boolean active);

    public void updatePassword(String password, int id);

    public void deleteEmployee(Employee employee);
    

    public void createProject(Project project, List<Employee> allEmployees);

    public ObservableList<Project> getAllProjects();

    public ObservableList<Project> loadAllActiveProjects();

    public ObservableList<Project> loadAllInactiveProjects();

    public ObservableList<Project> loadEmployeeProjects(int employeeId);

    public void updateProject(Project project, List<Employee> allEmployees);

    public void deleteProject(Project project);
    

    public int addTask(int userId, int projectId, String taskName);

    public ObservableList<Task> loadTasks(int userId, int projectId);

    public boolean updateTask(Task task);

    public boolean deleteTask(int taskId);

    public void saveTimeForTask(int taskId, int time, Date startTime, Date stopTime);

    public List<Integer> loadTimeForDates(int projectID, List<LocalDate> dates);
    
    public List<Project> loadWorkedOnProjectsBetweenDates(LocalDate startDate, LocalDate endDate, int employeeID);
    
    public XYChart.Series loadTimeForUsersProjects(LocalDate startDate, LocalDate endDate, int userID);
    
    public int loadAllProjectTimeBetweenDates(int projectID, LocalDate startDate, LocalDate endDate);
    
    public int loadAllEmployeeProjectTimeBetweenDates(int employeeID, int projectID, LocalDate startDate, LocalDate endDate);

    public boolean UpdateTimeForTask(TaskTime taskTime);

    public boolean DeleteTimeForTask(TaskTime taskTime);
    

    public void createCustomer(Customer customer);

    public ObservableList<Customer> getAllCustomers();

    public void updateCustomer(Customer customer);

    public void deleteCustomer(Customer customer);

}
