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

    public int addTask(int userId, int projectId, String taskName) {
        return dalFacade.addTask(userId, projectId, taskName);
    }

    public boolean updateTask(Task task) {
        return dalFacade.updateTask(task);
    }

    public boolean deleteTask(int taskId) {
        return dalFacade.deleteTask(taskId);
    }

    public void saveTimeForTask(int taskId, int time, Date startTime, Date stopTime) {
        dalFacade.saveTimeForTask(taskId, time, startTime, stopTime);
    }

    public boolean UpdateTimeToTask(TaskTime taskTime) {
        return dalFacade.UpdateTimeToTask(taskTime);
    }

    public boolean DeleteTimeToTask(TaskTime taskTime) {
        return dalFacade.DeleteTimeToTask(taskTime);
    }

    public ObservableList<Task> loadTasks(int userId, int projectId) {
        return dalFacade.loadTasks(userId, projectId);
    }

}
