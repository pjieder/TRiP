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
import trip.dal.dbmanagers.dbdao.AdminDBDAO;
import trip.dal.dbmanagers.dbdao.EmployeeDBDAO;
import trip.dal.dbmanagers.dbdao.ProjectDBDAO;
import trip.dal.dbmanagers.dbdao.UserDBDAO;

/**
 *
 * @author ander
 */
public class DalFacade implements IDalFacade {

    private EmployeeDBDAO employeeManager = new EmployeeDBDAO();
    private UserDBDAO userManager = new UserDBDAO();
    private AdminDBDAO adminManager = new AdminDBDAO();
    private ProjectDBDAO projectManager;

    public DalFacade() throws Exception
    {
        this.projectManager = new ProjectDBDAO();
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
    public ObservableList<Employee> loadEmployees() {
        return employeeManager.loadEmployees();
    }

    @Override
    public ObservableList<Task> loadTasks(int userId, int projectId) {

        ObservableList<Task> tasks = employeeManager.loadTasks(userId, projectId);

        for (Task task : tasks) {

            task.setTasks(employeeManager.loadTimeForTask(task.getId()));
            task.setTotalTime(employeeManager.getTaskTime(task.getId()));

        }

        return tasks;

    }

    @Override
    public ObservableList<Project> loadAllProjects(int employeeId) {
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
