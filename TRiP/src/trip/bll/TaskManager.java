/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.util.Date;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.TaskTime;
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
     */
    public Task addTask(int userId, int projectId, String taskName) {
        return dalFacade.addTask(userId, projectId, taskName);
    }

    /**
     * Loads all tasks stored in the database by the specified user on the specified project.
     *
     * @param userId The ID of
     * @param projectId
     * @return
     */
    public ObservableList<Task> loadTasks(int userId, int projectId) {
        return dalFacade.loadTasks(userId, projectId);
    }

    /**
     * Updates the specified task in the database.
     *
     * @param task The task that will update the previous task with the same ID.
     * @return A boolean value representing whether or not the task was updated.
     */
    public boolean updateTask(Task task) {
        return dalFacade.updateTask(task);
    }

    /**
     * Deletes the specified task from the database.
     *
     * @param task The task to be deleted.
     * @return A boolean value representing whether or not the task was deleted.
     */
    public boolean deleteTask(Task task) {
        return dalFacade.deleteTask(task);
    }

    /**
     * Saves the time having been worked on the task in the database.
     *
     * @param task The task being worked on.
     * @param time The total amount of time having been worked on the task in seconds.
     * @param startTime The starttime of when the work began.
     * @param stopTime The endtime of when the work ended.
     */
    public void saveTimeForTask(Task task, int time, Date startTime, Date stopTime) {
        dalFacade.saveTimeForTask(task, time, startTime, stopTime);
    }

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param taskTime The taskTime that will update the previous taskTime with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     */
    public boolean UpdateTimeForTask(TaskTime taskTime) {
        return dalFacade.UpdateTimeForTask(taskTime);
    }

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param taskTime The taskTime to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     */
    public boolean DeleteTimeForTask(TaskTime taskTime) {
        return dalFacade.DeleteTimeForTask(taskTime);
    }

}
