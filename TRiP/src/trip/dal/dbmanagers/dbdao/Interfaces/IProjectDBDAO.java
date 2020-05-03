/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import trip.be.Project;

/**
 *
 * @author Jacob
 */
public interface IProjectDBDAO {

    /**
     * Saves the newly created project in the database.
     *
     * @param project The project to be saved.
     */
    public void createProject(Project project);

    /**
     * Loads all saved projects in the database.
     *
     * @return An observablelist containing all projects stored.
     */
    public ObservableList<Project> loadAllProjects();

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
     * @param employeeID The ID of the employee.
     * @return Returns an observablelist containing all projects assigned to the specified employee.
     */
    public ObservableList<Project> loadEmployeeProjects(int employeeID);

    /**
     * Loads the total amount of time worked on the project by the specified employee.
     *
     * @param employeeID The ID of the employee working on the project.
     * @param projectID The ID of the project being worked on.
     * @return An int value representing the total amount of time worked on the project by the specified user in seconds.
     */
    public int loadProjectTime(int employeeID, int projectID);

    /**
     * Loads the total amount of time having been used on the project by all users.
     *
     * @param projectID The ID of the project.
     * @return An int value representing the total amount of time worked on the project in seconds.
     */
    public int loadTotalProjectTime(int projectID);

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
     * Loads the total amount of time having been registered on the specified date.
     *
     * @param date The date being searched for.
     * @return An int value representing the total amount of work done on the specified date in seconds.
     */
    public int loadAllProjectTimeForDay(LocalDate date);

    /**
     * Loads the total amount of time having been registered on the specified project on the date.
     *
     * @param projectID The project ID of the project being searched for.
     * @param date The date being searched for.
     * @return An int value representing the total amount of work done on the specified project on the specified date in seconds.
     */
    public int loadProjectTimeForDay(int projectID, LocalDate date);

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
     * Updates the specified project in the database.
     *
     * @param project The project that will update the previous project with the same ID.
     */
    public void updateProject(Project project);

    /**
     * Deletes the specified project from the database.
     *
     * @param project The project to be deleted.
     */
    public void deleteProject(Project project);

}
