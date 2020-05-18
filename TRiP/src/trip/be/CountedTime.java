/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.be;

import java.util.Date;

/**
 *
 * @author ander
 */
public class CountedTime {

    private int id;
    private int time;
    private Date startTime;
    private Date stopTime;
    private boolean billable;

    public CountedTime(int time, Date startTime, Date stopTime) {
        this.time = time;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.billable = true;
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

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    @Override
    public boolean equals(Object obj) {

        CountedTime taskTime = (CountedTime) obj;

        if (taskTime.getId() == this.getId()) {
            return true;
        }
        return false;
    }
}
