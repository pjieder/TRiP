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

    /**
     * Saves the newly created project in the database together with the employees assigned to the project.
     *
     * @param project The project to be saved.
     * @param allEmployees The employees that should be registered to the project.
     */
    public void createProject(Project project, List<Employee> allEmployees) {
        projectManager.createProject(project, allEmployees);
    }

    /**
     * Loads all saved projects in the database.
     *
     * @return An observablelist containing all projects stored.
     */
    public ObservableList<Project> getAllProjects() {
        return projectManager.getAllProjects();
    }

    /**
     * Loads all active projects stored in the database.
     *
     * @return An observablelist containing all active projects stored.
     */
    public ObservableList<Project> loadAllActiveProjects() {
        return projectManager.loadAllActiveProjects();
    }

    /**
     * Loads all inactiev projects stored in the database.
     *
     * @return An observablelist containing all the inactive projects stored.
     */
    public ObservableList<Project> loadAllInactiveProjects() {
        return projectManager.loadAllInactiveProjects();
    }

    /**
     * Loads all stored projects assigned to the specified employee.
     *
     * @param employeeId The ID of the employee.
     * @return Returns an observablelist containing all projects assigned to the specified employee.
     */
    public ObservableList<Project> loadAllEmployeeProjects(int employeeId) {
        return projectManager.loadEmployeeProjects(employeeId);

    }

    /**
     * Loads all projects having been worked on between the specified dates by the specified employee.
     *
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @param employeeID The ID of the specified employee.
     * @return A list containing all projects having been worked on by the specified employee between the two dates.
     */
    public List<Project> loadWorkedOnProjectsBetweenDates(LocalDate startDate, LocalDate endDate, int employeeID) {
        return projectManager.loadWorkedOnProjectsBetweenDates(startDate, endDate, employeeID);
    }

    /**
     * Loads the total amount of time having been registered on the specified project between a specified timespan.
     *
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @return An int value representing the total amount of time having been used in seconds.
     */
    public int loadAllProjectTimeBetweenDates(int projectID, LocalDate startDate, LocalDate endDate) {
        return projectManager.loadAllProjectTimeBetweenDates(projectID, startDate, endDate);
    }

    /**
     * Loads the total amount of time having been registered on the specifiec project by the specified employee between the timespan.
     *
     * @param employeeID The ID of the specified employee .
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @return An int value representing the total amount of time the specified employe have been working on the project in seconds.
     */
    public int loadAllEmployeeProjectTimeBetweenDates(int employeeID, int projectID, LocalDate startDate, LocalDate endDate) {
        return projectManager.loadAllEmployeeProjectTimeBetweenDates(employeeID, projectID, startDate, endDate);
    }

    /**
     * Updates the specified project in the database.
     *
     * @param project The project that will update the previous project with the same ID.
     * @param allEmployees The employees that should be registered to the project.
     */
    public void updateProject(Project project, List<Employee> allEmployees) {
        projectManager.updateProject(project, allEmployees);
    }

    /**
     * Deletes the specified project from the database.
     *
     * @param project The project to be deleted.
     */
    public void deleteProject(Project project) {
        projectManager.deleteProject(project);
    }

    /**
     * Searches for projects matching the search term.
     *
     * @param projectName The name of the project being searched for.
     * @param projectList The list of all projects the search should be based on.
     * @return An observablelist of call the projects found matching the search term.
     */
    public ObservableList<Project> searchProjects(String projectName, ObservableList<Project> projectList) {
        return projectManager.searchProjects(projectName, projectList);
    }

    /**
     * Calculates the chart series for the linegraph representing the total amount of time having been worked on the selected project between the specified days.
     *
     * @param projectID The ID of the project that the series is based upon.
     * @param startDate The startdate of the timeframe.
     * @param endDate The enddate of the timeframe.
     * @return A series containing all the stored data to be displayed.
     */
    public XYChart.Series calculateGraphLine(int projectID, LocalDate startDate, LocalDate endDate) {
        return projectManager.calculateGraphLine(projectID, startDate, endDate);
    }

    /**
     * Calculates the chart series for the bargraph representing the total amount of time individual users have been working and on what tasks between the specified days.
     *
     * @param startDate The startdate of the timeframe.
     * @param endDate The enddate of the timeframe.
     * @param employeeID The Id of the employee that the series is based upon.
     * @return
     */
    public XYChart.Series calculateGraphBar(LocalDate startDate, LocalDate endDate, int employeeID) {
        return projectManager.calculateGraphBar(startDate, endDate, employeeID);
    }
}
