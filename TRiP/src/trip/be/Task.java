/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.be;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ander
 */
public class Task {
    
    private int id;
    private String name;
    private ObservableList<TaskTime> timeTasks = FXCollections.observableArrayList();

    public Task(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<TaskTime> getTimeTasks() {
        return timeTasks;
    }

    public void setTasks(ObservableList<TaskTime> timeTasks) {
        this.timeTasks = timeTasks;
    }
    
        @Override
    public boolean equals(Object obj) {

        Task task = (Task) obj;

        if (task.getId() == this.getId()) {
            return true;
        }
        return false;

    }
    
    
}
