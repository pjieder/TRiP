/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import java.util.Date;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.TaskTime;

/**
 *
 * @author ander
 */
public interface ITaskDBDAO {

    /**
     * Saves the newly created task in the database.
     *
     * @param userId The ID of the user working on the task.
     * @param projectId The ID of the project that the task is associated to.
     * @param taskName The name of the task.
     * @return The ID of the newly created task.
     */
    public int addTask(int userId, int projectId, String taskName);

    /**
     * Loads all tasks stored in the database by the specified user on the specified project.
     *
     * @param userId The ID of
     * @param projectId
     * @return An observablelist containing all stored tasks for the specified user and project.
     */
    public ObservableList<Task> loadTasks(int userId, int projectId);

    /**
     * Updates the specified task in the database.
     *
     * @param task The task that will update the previous task with the same ID.
     * @return A boolean value representing whether or not the task was updated.
     */
    public boolean updateTask(Task task);

    /**
     * Deletes the specified task from the database.
     *
     * @param taskId The ID of the task to be deleted.
     * @return A boolean value representing whether or not the task was deleted.
     */
    public boolean deleteTask(int taskId);

    /**
     * Saves the time having been worked on the task in the database.
     *
     * @param taskId The ID of the task being worked on.
     * @param time The total amount of time having been worked on the task in seconds.
     * @param startTime The starttime of when the work began.
     * @param stopTime The endtime of when the work ended.
     */
    public void addTimeForTask(int taskId, int time, Date startTime, Date stopTime);

    /**
     * Returns the total amount of time having been used on the specified task in seconds.
     *
     * @param taskID The ID of the task.
     * @return An int value representing the total amount of time having been used on the task in seconds.
     */
    public int getTaskTime(int taskID);

    /**
     * Loads all time stored for the task.
     *
     * @param taskId The ID of the task being searched for.
     * @return An observablelist containing all time registered for the task.
     */
    public ObservableList<TaskTime> loadTimeForTask(int taskId);

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param taskTime The taskTime that will update the previous taskTime with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     */
    public boolean UpdateTimeForTask(TaskTime taskTime);

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param taskTime The taskTime to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     */
    public boolean DeleteTimeForTask(TaskTime taskTime);

}
