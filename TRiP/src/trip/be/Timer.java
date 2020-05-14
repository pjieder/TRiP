/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.be;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Label;
import trip.utilities.TimeConverter;

/**
 *
 * @author ander
 */
public class Timer implements Runnable{
    
    private int time = 0;
    private Task task;
    private Date startTime;
    private Date stopTime;
    private Label numberLabel;
    ExecutorService executor = null;

    /**
     * Starts the count of the timer for the task instance variable.
     */
    @Override
    public void run() {

        try {
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                time++;

                Platform.runLater(() -> {
                    displayTime();
                });
            }
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Displays the current amount of time that the timer has run.
     */
    public void displayTime() {
        numberLabel.setText(TimeConverter.convertSecondsToString(time));
    }

    /**
     * Stores the taskID and number label as instance variables and starts the timer.
     * @param task The task being timed.
     * @param numberLabel The label to display the amount of time the timer has run.
     */
    public void startTimer(Task task, Label numberLabel) {
        this.task = task;
        this.numberLabel = numberLabel;
        this.startTime = new Date();
        time = 0;
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
    }
    
    /**
     * Stops the timer and resets the label.
     * @return Returns the amount of time that the timer has been running.
     */
    public int stopTimer()
    {
        this.stopTime = new Date();
        numberLabel.setText("00:00:00");
        executor.shutdownNow();
        return time;
    }
    
    /**
     * Checks whether or not the timer is still running.
     * @return A boolean representing whether or not the timer is still running.
     */
    public boolean isEnabled()
    {
        if (executor == null)
        {
            return false;
        }
        return !executor.isShutdown();
    }

    public Task getTask ()
    {
        return task;
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
    
}
