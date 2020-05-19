/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import java.sql.SQLException;
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
     * @throws java.sql.SQLException
     */
    public void createProject(Project project) throws SQLException;

    /**
     * Loads all saved projects in the database.
     *
     * @return An observablelist containing all projects stored.
     * @throws java.sql.SQLException
     */
    public ObservableList<Project> loadAllProjects() throws SQLException;

    /**
     * Loads all active projects stored in the database.
     *
     * @return An observablelist containing all active projects stored.
     * @throws java.sql.SQLException
     */
    public ObservableList<Project> loadAllActiveProjects() throws SQLException;

    /**
     * Loads all inactiev projects stored in the database.
     *
     * @return An observablelist containing all the inactive projects stored.
     * @throws java.sql.SQLException
     */
    public ObservableList<Project> loadAllInactiveProjects() throws SQLException;

    /**
     * Loads all stored projects assigned to the specified employee.
     *
     * @param employeeID The ID of the employee.
     * @return Returns an observablelist containing all projects assigned to the specified employee.
     * @throws java.sql.SQLException
     */
    public ObservableList<Project> loadEmployeeProjects(int employeeID) throws SQLException;

    /**
     * Loads the total amount of time worked on the project by the specified employee.
     *
     * @param employeeID The ID of the employee working on the project.
     * @param projectID The ID of the project being worked on.
     * @return An int value representing the total amount of time worked on the project by the specified user in seconds.
     * @throws java.sql.SQLException
     */
    public int loadProjectTime(int employeeID, int projectID) throws SQLException;

    /**
     * Loads the total amount of time having been used on the project by all users.
     *
     * @param projectID The ID of the project.
     * @return An int value representing the total amount of time worked on the project in seconds.
     * @throws java.sql.SQLException
     */
    public int loadTotalProjectTime(int projectID) throws SQLException;

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
    public int loadAllProjectTimeBetweenDates(int projectID, LocalDate startDate, LocalDate endDate, boolean isBillable) throws SQLException;

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
    public int loadAllEmployeeProjectTimeBetweenDates(int employeeID, int projectID, LocalDate startDate, LocalDate endDate, boolean isBillable) throws SQLException;

    /**
     * Loads the total amount of time having been registered on the specified date.
     *
     * @param date The date being searched for.
     * @return An int value representing the total amount of work done on the specified date in seconds.
     * @throws java.sql.SQLException
     */
    public int loadAllProjectTimeForDay(LocalDate date) throws SQLException;

    /**
     * Loads the total amount of time having been registered on the specified project on the date.
     *
     * @param projectID The project ID of the project being searched for.
     * @param date The date being searched for.
     * @return An int value representing the total amount of work done on the specified project on the specified date in seconds.
     * @throws java.sql.SQLException
     */
    public int loadProjectTimeForDay(int projectID, LocalDate date) throws SQLException;

    /**
     * Loads all projects having been worked on between the specified dates by the specified employee.
     *
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @param employeeID The ID of the specified employee.
     * @return A list containing all projects having been worked on by the specified employee between the two dates.
     * @throws java.sql.SQLException
     */
    public List<Project> loadWorkedOnProjectsBetweenDates(LocalDate startDate, LocalDate endDate, int employeeID) throws SQLException;

    /**
     * Calculates the chart series for the bargraph representing the total amount of time individual users have been working and on what tasks between the specified days.
     *
     * @param startDate The startdate of the timeframe.
     * @param endDate The enddate of the timeframe.
     * @param employeeID The Id of the employee that the series is based upon.
     * @return A series containing all the stored data to be displayed.
     * @throws java.sql.SQLException
     */
    public XYChart.Series loadTimeForUsersProjects(LocalDate startDate, LocalDate endDate, int employeeID) throws SQLException;

    /**
     * Updates the specified project in the database.
     *
     * @param project The project that will update the previous project with the same ID.
     * @throws java.sql.SQLException
     */
    public void updateProject(Project project) throws SQLException;

    /**
     * Updates whether or not the specified project is active.
     *
     * @param project The project to be updated.
     * @param active Boolean representing whether or not the project should be active or inactive.
     * @throws java.sql.SQLException
     */
    public void updateProjectActive(Project project, boolean active) throws SQLException;

    /**
     * Deletes the specified project from the database.
     *
     * @param project The project to be deleted.
     * @throws java.sql.SQLException
     */
    public void deleteProject(Project project) throws SQLException;

}
