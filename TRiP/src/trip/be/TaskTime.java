/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.be;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author ander
 */
public class TaskTime {

    private int id;
    private int time;
    private Date startTime;
    private Date stopTime;

    public TaskTime(int time, Date startTime, Date stopTime) {
        this.time = time;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public boolean equals(Object obj) {

        TaskTime taskTime = (TaskTime) obj;

        if (taskTime.getId() == this.getId()) {
            return true;
        }
        return false;

    }

}
