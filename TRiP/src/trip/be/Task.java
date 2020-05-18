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
    private String employee;
    private int totalTime;
    private int unbillabletime;
    private ObservableList<CountedTime> countedTime = FXCollections.observableArrayList();

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

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getUnbillabletime() {
        return unbillabletime;
    }

    public void setUnbillabletime(int unbillabletime) {
        this.unbillabletime = unbillabletime;
    }
    
    public ObservableList<CountedTime> getCountedTime() {
        return countedTime;
    }

    public void setCountedTime(ObservableList<CountedTime> countedTime) {
        this.countedTime = countedTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
