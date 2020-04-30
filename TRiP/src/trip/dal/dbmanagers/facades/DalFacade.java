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
import trip.be.Customer;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Roles;
import trip.be.Task;
import trip.be.TaskTime;
import trip.dal.dbmanagers.dbdao.AdminDBDAO;
import trip.dal.dbmanagers.dbdao.CustomerDBDAO;
import trip.dal.dbmanagers.dbdao.EmployeeDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.IAdminDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.ICustomerDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.IEmployeeDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.IProjectDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.ITaskDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.IUserDBDAO;
import trip.dal.dbmanagers.dbdao.ProjectDBDAO;
import trip.dal.dbmanagers.dbdao.TaskDBDAO;
import trip.dal.dbmanagers.dbdao.UserDBDAO;

/**
 *
 * @author ander
 */
public class DalFacade implements IDalFacade {

    private IEmployeeDBDAO employeeDBDAO;
    private IUserDBDAO userDBDAO;
    private IAdminDBDAO adminDBDAO;
    private IProjectDBDAO projectManager;
    private ITaskDBDAO taskManager;
    private ICustomerDBDAO customerManager;

    public DalFacade() {

        employeeDBDAO = new EmployeeDBDAO();
        userDBDAO = new UserDBDAO();
        adminDBDAO = new AdminDBDAO();
        projectManager = new ProjectDBDAO();
        taskManager = new TaskDBDAO();
        customerManager = new CustomerDBDAO();
    }

    @Override
    public Employee login(String username, String password) {

        Employee employee = null;
        int employeeId = employeeDBDAO.isLoginCorrect(username, password);

        if (employeeId != -1) {
            Roles role = employeeDBDAO.getRoleById(employeeId);

            if (role == Roles.USER) {
                employee = userDBDAO.getUserById(employeeId);
            } else if (role == Roles.ADMIN) {
                employee = adminDBDAO.getAdminById(employeeId);
            }
            return employee;
        }
        return employee;
    }

    @Override
    public void createEmployee(Employee employee, String password) {
        employeeDBDAO.createEmployee(employee, password);
    }

    @Override
    public ObservableList<Employee> loadActiveEmployees() {
        return employeeDBDAO.loadActiveEmployees();
    }

    @Override
    public ObservableList<Employee> loadInactiveEmployees() {
        return employeeDBDAO.loadInactiveEmployees();
    }

    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) {
        return employeeDBDAO.loadEmployeesAssignedToProject(projectId);
    }

    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) {
        return employeeDBDAO.loadEmployeesAssignedToProject(projectId, isActive);
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeDBDAO.updateEmployee(employee);
    }

    @Override
    public void updateEmployeeActive(int employeeId, boolean active) {
        employeeDBDAO.updateEmployeeActive(employeeId, active);
    }

    @Override
    public void updatePassword(String username, String password, int id) {
        employeeDBDAO.updatePassword(username, password, id);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeDBDAO.deleteEmployee(employee);
    }

    
    @Override
    public void createProject(Project project, List<Employee> allEmployees) {
        projectManager.createProject(project);
        customerManager.addCustomerToProject(project.getCustomer().getId(), project.getId());
        for (Employee employee : allEmployees) {
            employeeDBDAO.addEmployeeToProject(employee.getId(), project.getId());
        }
    }

    @Override
    public ObservableList<Project> getAllProjects() {
        return projectManager.loadAllProjects();
    }

    @Override
    public ObservableList<Project> loadAllActiveProjects() {
        ObservableList<Project> allActiveProjects = projectManager.loadAllActiveProjects();
        for (Project project : allActiveProjects) {
            project.setTotalTime(projectManager.loadTotalProjectTime(project.getId()));
            project.setCustomer(customerManager.getCustomerForProject(project.getId()));
        }
        return allActiveProjects;
    }

    @Override
    public ObservableList<Project> loadAllInactiveProjects() {
        ObservableList<Project> allActiveProjects = projectManager.loadAllInactiveProjects();
        for (Project project : allActiveProjects) {
            project.setTotalTime(projectManager.loadTotalProjectTime(project.getId()));
            project.setCustomer(customerManager.getCustomerForProject(project.getId()));
        }
        return allActiveProjects;
    }

    @Override
    public ObservableList<Project> loadEmployeeProjects(int employeeId) {
        ObservableList<Project> allUserProjects = projectManager.loadEmployeeProjects(employeeId);
        for (Project project : allUserProjects) {
            project.setTotalTime(projectManager.loadProjectTime(employeeId, project.getId()));
        }
        return allUserProjects;
    }

    @Override
    public void updateProject(Project project, List<Employee> allEmployees) {
        projectManager.updateProject(project);
        customerManager.removeCustomerFromProject(project.getId());
        customerManager.addCustomerToProject(project.getCustomer().getId(), project.getId());
        employeeDBDAO.removeAllEmployeesFromProject(project.getId());

        for (Employee employee : allEmployees) {
            employeeDBDAO.addEmployeeToProject(employee.getId(), project.getId());
        }
    }

    @Override
    public void deleteProject(Project project) {
        projectManager.deleteProject(project);
    }

    
    @Override
    public int addTask(int userId, int projectId, String taskName) {
        return taskManager.addTask(userId, projectId, taskName);
    }

    @Override
    public ObservableList<Task> loadTasks(int userId, int projectId) {

        ObservableList<Task> tasks = taskManager.loadTasks(userId, projectId);

        for (Task task : tasks) {

            task.setTasks(taskManager.loadTimeForTask(task.getId()));
            task.setTotalTime(taskManager.getTaskTime(task.getId()));
        }
        return tasks;
    }

    @Override
    public boolean updateTask(Task task) {
        return taskManager.updateTask(task);
    }

    @Override
    public boolean deleteTask(int taskId) {
        return taskManager.deleteTask(taskId);
    }

    @Override
    public void saveTimeForTask(int taskId, int time, Date startTime, Date stopTime) {
        taskManager.addTimeToTask(taskId, time, startTime, stopTime);
    }

    @Override
    public List<Integer> loadTimeForDates(int projectID, List<LocalDate> dates) {

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

    @Override
    public boolean UpdateTimeToTask(TaskTime taskTime) {
        return taskManager.UpdateTimeToTask(taskTime);
    }

    @Override
    public boolean DeleteTimeToTask(TaskTime taskTime) {
        return taskManager.DeleteTimeToTask(taskTime);
    }

    
    @Override
    public void createCustomer(Customer customer) {
        customerManager.createCustomer(customer);
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        return customerManager.getAllCustomers();
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerManager.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customerManager.deleteCustomer(customer);
    }

}
