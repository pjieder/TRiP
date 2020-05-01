/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import trip.be.Employee;
import trip.be.Project;
import trip.bll.ProjectManager;

/**
 *
 * @author Jacob
 */
public class ProjectModel {

    private final ProjectManager projectManager;

    public ProjectModel() {
        projectManager = new ProjectManager();
    }

    public ObservableList<Project> getAllProjects() {
        return projectManager.getAllProjects();
    }

    public ObservableList<Project> loadAllActiveProjects() {
        return projectManager.loadAllActiveProjects();
    }

    public ObservableList<Project> loadAllInactiveProjects() {
        return projectManager.loadAllInactiveProjects();
    }

    public ObservableList<Project> loadAllUserProjects(int employeeId) {
        return projectManager.loadEmployeeProjects(employeeId);

    }

    public List<Project> loadWorkedOnProjectsBetweenDates(LocalDate startDate, LocalDate endDate, int employeeID) {
        return projectManager.loadWorkedOnProjectsBetweenDates(startDate, endDate, employeeID);
    }

    public int loadAllProjectTimeBetweenDates(int projectID, LocalDate startDate, LocalDate endDate) {
        return projectManager.loadAllProjectTimeBetweenDates(projectID, startDate, endDate);
    }

    public int loadAllEmployeeProjectTimeBetweenDates(int employeeID, int projectID, LocalDate startDate, LocalDate endDate) {
        return projectManager.loadAllEmployeeProjectTimeBetweenDates(employeeID, projectID, startDate, endDate);
    }

    public void createProject(Project project, List<Employee> allEmployees) {
        projectManager.createProject(project, allEmployees);
    }

    public void updateProject(Project project, List<Employee> allEmployees) {
        projectManager.updateProject(project, allEmployees);
    }

    public void deleteProject(Project project) {
        projectManager.deleteProject(project);
    }

    public ObservableList<Project> searchProjects(String projectName, ObservableList<Project> projectList) {
        return projectManager.searchProjects(projectName, projectList);
    }

    public XYChart.Series calculateGraphLine(int projectID, LocalDate startDate, LocalDate endDate) {
        return projectManager.calculateGraphLine(projectID, startDate, endDate);
    }

    public XYChart.Series calculateGraphBar(LocalDate startDate, LocalDate endDate, int employeeID) {
        return projectManager.calculateGraphBar(startDate, endDate, employeeID);
    }
}
