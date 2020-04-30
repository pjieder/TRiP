/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import java.util.Date;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.TaskTime;
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

    public int addTask(int userId, int projectId, String taskName) {
        return taskManager.addTask(userId, projectId, taskName);
    }

    public ObservableList<Task> loadTasks(int userId, int projectId) {
        return taskManager.loadTasks(userId, projectId);
    }

    public boolean updateTask(Task task) {
        return taskManager.updateTask(task);
    }

    public boolean deleteTask(int taskId) {
        return taskManager.deleteTask(taskId);
    }

    public void saveTimeForTask(int taskId, int time, Date startTime, Date stopTime) {
        taskManager.saveTimeForTask(taskId, time, startTime, stopTime);
    }

    public boolean UpdateTimeToTask(TaskTime taskTime) {
        return taskManager.UpdateTimeToTask(taskTime);
    }

    public boolean DeleteTimeToTask(TaskTime taskTime) {
        return taskManager.DeleteTimeToTask(taskTime);
    }

}
