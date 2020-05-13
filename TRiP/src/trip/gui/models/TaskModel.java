/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.CountedTime;
import trip.bll.TaskManager;

/**
 *
 * @author ander
 */
public class TaskModel {

    private final TaskManager taskManager;

    public TaskModel() {
        taskManager = new TaskManager();
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
    public Task addTask(int userId, int projectId, String taskName) throws SQLException {
        return taskManager.addTask(userId, projectId, taskName);
    }

    /**
     * Loads all tasks stored in the database by the specified user on the specified project.
     *
     * @param userId The ID of employee
     * @param projectId The ID of the project being searched for.
     * @return A list of all tasks stored.
     * @throws java.sql.SQLException
     */
    public ObservableList<Task> loadTasks(int userId, int projectId) throws SQLException {
        return taskManager.loadTasks(userId, projectId);
    }

    public ObservableList<Task> loadAllUniqueTasksDates(int projectID, LocalDate startDate, LocalDate endDate) throws SQLException {
        return taskManager.loadAllUniqueTasksDates(projectID, startDate, endDate);
    }

    /**
     * Updates the specified task in the database.
     *
     * @param task The task that will update the previous task with the same ID.
     * @return A boolean value representing whether or not the task was updated.
     * @throws java.sql.SQLException
     */
    public boolean updateTask(Task task) throws SQLException {
        return taskManager.updateTask(task);
    }

    /**
     * Deletes the specified task from the database.
     *
     * @param task The task to be deleted.
     * @return A boolean value representing whether or not the task was deleted.
     * @throws java.sql.SQLException
     */
    public boolean deleteTask(Task task) throws SQLException {
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
    public void saveTimeForTask(Task task, int time, Date startTime, Date stopTime) throws SQLException {
        taskManager.saveTimeForTask(task, time, startTime, stopTime);
    }

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param taskTime The taskTime that will update the previous taskTime with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     * @throws java.sql.SQLException
     */
    public boolean UpdateTimeForTask(CountedTime taskTime) throws SQLException {
        return taskManager.UpdateTimeForTask(taskTime);
    }

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param taskTime The taskTime to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     * @throws java.sql.SQLException
     */
    public boolean DeleteTimeForTask(CountedTime taskTime) throws SQLException {
        return taskManager.DeleteTimeForTask(taskTime);
    }

}
