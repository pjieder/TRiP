/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.CountedTime;
import trip.dal.dbmanagers.facades.DalFacade;
import trip.dal.dbmanagers.facades.IDalFacade;

/**
 *
 * @author ander
 */
public class TaskManager {

    private IDalFacade dalFacade;

    public TaskManager() {
        dalFacade = new DalFacade();
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
    public Task addTask(int userId, int projectId, String taskName) throws SQLException{
        return dalFacade.addTask(userId, projectId, taskName);
    }

    /**
     * Loads all tasks stored in the database by the specified user on the specified project.
     *
     * @param userId The ID of
     * @param projectId
     * @return
     * @throws java.sql.SQLException
     */
    public ObservableList<Task> loadTasks(int userId, int projectId) throws SQLException{
        return dalFacade.loadTasks(userId, projectId);
    }

    /**
     * Loads all unique tasks having been worked on between two specified dates within the specified project.
     * @param projectID The id of the project searching tasks for.
     * @param startDate The startdate of the time span.
     * @param endDate The enddate of the time span.
     * @return An observablelist containing all the stored tasks searched for.
     * @throws SQLException 
     */
    public ObservableList<Task> loadAllUniqueTasksDates(int projectID, LocalDate startDate, LocalDate endDate) throws SQLException
    {
        return dalFacade.loadAllUniqueTasksDates(projectID, startDate, endDate);
    }
    
    /**
     * Updates the specified task in the database.
     *
     * @param task The task that will update the previous task with the same ID.
     * @return A boolean value representing whether or not the task was updated.
     * @throws java.sql.SQLException
     */
    public boolean updateTask(Task task) throws SQLException{
        return dalFacade.updateTask(task);
    }

    /**
     * Deletes the specified task from the database.
     *
     * @param task The task to be deleted.
     * @return A boolean value representing whether or not the task was deleted.
     * @throws java.sql.SQLException
     */
    public boolean deleteTask(Task task) throws SQLException{
        return dalFacade.deleteTask(task);
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
    public void saveTimeForTask(Task task, int time, Date startTime, Date stopTime) throws SQLException{
        dalFacade.saveTimeForTask(task, time, startTime, stopTime);
    }

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param countedTime The counted time that will update the previous counred time with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     * @throws java.sql.SQLException
     */
    public boolean UpdateTimeForTask(CountedTime countedTime) throws SQLException{
        return dalFacade.UpdateTimeForTask(countedTime);
    }

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param countedTime The tcounted time to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     * @throws java.sql.SQLException
     */
    public boolean DeleteTimeForTask(CountedTime countedTime) throws SQLException{
        return dalFacade.DeleteTimeForTask(countedTime);
    }

}
