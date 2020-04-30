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
    private int taskId = 0;
    private Date startTime;
    private Date stopTime;
    private Label numberLabel;
    ExecutorService executor = null;

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

    public void displayTime() {
        numberLabel.setText(TimeConverter.convertSecondsToString(time));
    }

    public void startTimer(int taskId, Label numberLabel) {
        this.taskId = taskId;
        this.numberLabel = numberLabel;
        this.startTime = new Date();
        time = 0;
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
    }
    
    public int stopTimer()
    {
        this.stopTime = new Date();
        numberLabel.setText("00:00:00");
        executor.shutdownNow();
        return time;
    }
    
    public boolean isEnabled()
    {
        if (executor == null)
        {
            return false;
        }
        return !executor.isShutdown();
    }

    public int getTaskId ()
    {
        return taskId;
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
    
    
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(TimeConverter.convertDateToString(date));
    }
    
}
