/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.Timer;
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

    public void saveTimeForTask(Timer timer) {
        dalFacade.saveTimeForTask(timer);
    }

    public ObservableList<Task> loadTasks(int userId, int projectId) {
        return dalFacade.loadTasks(userId, projectId);
    }

}
