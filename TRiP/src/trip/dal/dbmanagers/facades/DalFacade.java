/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.facades;

import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Roles;
import trip.be.Task;
import trip.be.Timer;
import trip.dal.dbmanagers.dbdao.AdminDBDAO;
import trip.dal.dbmanagers.dbdao.EmployeeDBDAO;
import trip.dal.dbmanagers.dbdao.Interfaces.IAdminDBDAO;
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

    private IEmployeeDBDAO employeeManager;
    private IUserDBDAO userManager;
    private IAdminDBDAO adminManager;
    private IProjectDBDAO projectManager;
    private ITaskDBDAO taskManager;

    public DalFacade() {

        employeeManager = new EmployeeDBDAO();
        userManager = new UserDBDAO();
        adminManager = new AdminDBDAO();
        projectManager = new ProjectDBDAO();
        taskManager = new TaskDBDAO();
    }

    @Override
    public Employee login(String username, String password) {

        Employee employee = null;
        int employeeId = employeeManager.isLoginCorrect(username, password);

        if (employeeId != -1) {
            Roles role = employeeManager.getRoleById(employeeId);

            if (role == Roles.USER) {
                employee = userManager.getUserById(employeeId);
            } else if (role == Roles.ADMIN) {
                employee = adminManager.getAdminById(employeeId);
            }

            return employee;

        }

        return employee;
    }

    @Override
    public void createUser(Employee employee, String password) {
        employeeManager.createEmployee(employee, password);
    }

    @Override
    public int addTask(int userId, int projectId, String taskName) {
        return taskManager.addTask(userId, projectId, taskName);
    }

    @Override
    public void saveTimeForTask(Timer timer) {
        taskManager.addTimeToTask(timer);
    }

    @Override
    public ObservableList<Employee> loadEmployees() {
        return employeeManager.loadEmployees();
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
    public void createProject(Project project) {
        projectManager.createProject(project);
    }

    @Override
    public ObservableList<Project> loadAllActiveProjects(int employeeId) {
        ObservableList<Project> allActiveProjects = projectManager.getAllActiveProjects();
        for (Project project : allActiveProjects) {
            project.setTotalTime(projectManager.getProjectTime(employeeId, project.getId()));
        }
        return allActiveProjects;
    }

    @Override
    public ObservableList<Project> loadUserProjects(int employeeId) {
        ObservableList<Project> allUserProjects = projectManager.getEmployeeProjects(employeeId);
        for (Project project : allUserProjects) {
            project.setTotalTime(projectManager.getProjectTime(employeeId, project.getId()));
        }
        return allUserProjects;
    }

}
