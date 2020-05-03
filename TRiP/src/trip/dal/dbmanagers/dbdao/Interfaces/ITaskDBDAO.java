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
import trip.be.Timer;

/**
 *
 * @author ander
 */
public interface ITaskDBDAO {

    public int addTask(int userId, int projectId, String taskName);

    public ObservableList<Task> loadTasks(int userId, int projectId);

    public boolean updateTask(Task task);

    public boolean deleteTask(int taskId);

    public void addTimeForTask(int taskId, int time, Date startTime, Date stopTime);

    public int getTaskTime(int taskID);

    public ObservableList<TaskTime> loadTimeForTask(int taskId);

    public boolean UpdateTimeForTask(TaskTime taskTime);

    public boolean DeleteTimeForTask(TaskTime taskTime);

}
