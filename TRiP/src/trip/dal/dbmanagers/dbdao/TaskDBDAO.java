/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.TaskTime;
import trip.dal.dbaccess.DBSettings;
import trip.dal.dbmanagers.dbdao.Interfaces.ITaskDBDAO;
import trip.dal.dbmanagers.dbdao.utilities.DatabaseLogger;
import trip.utilities.TimeConverter;

/**
 *
 * @author ander
 */
public class TaskDBDAO implements ITaskDBDAO {

    /**
     * Saves the newly created task in the database.
     *
     * @param userId The ID of the user working on the task.
     * @param projectId The ID of the project that the task is associated to.
     * @param taskName The name of the task.
     * @return The newly created task.
     * @throws java.sql.SQLException
     */
    @Override
    public Task addTask(int userId, int projectId, String taskName) throws SQLException {
        Connection con = null;
        Task task = new Task(taskName);
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Task (projID, employeeID, name) "
                    + "VALUES (?,?,?);";
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, projectId);
            stmt.setInt(2, userId);
            stmt.setString(3, taskName);

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int ID = generatedKeys.getInt(1);
                task.setId(ID);
                return task;
            }

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Created task with ID: " + task.getId() + " (" + taskName + ")");
        }
        return task;
    }

    /**
     * Loads all tasks stored in the database by the specified user on the specified project.
     *
     * @param userId The ID of
     * @param projectId
     * @return An observablelist containing all stored tasks for the specified user and project.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Task> loadTasks(int userId, int projectId) throws SQLException {

        Connection con = null;
        ObservableList<Task> tasks = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Task WHERE Task.employeeID = ? AND Task.projID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, userId);
            stmt.setInt(2, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                String name = rs.getString("name");

                Task task = new Task(name);
                task.setId(id);
                tasks.add(task);
            }

            return tasks;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Updates the specified task in the database.
     *
     * @param task The task that will update the previous task with the same ID.
     * @return A boolean value representing whether or not the task was updated.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updateTask(Task task) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Task SET Task.name = ? WHERE Task.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, task.getName());
            stmt.setInt(2, task.getId());

            stmt.executeUpdate();

            return true;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated task with ID: " + task.getId() + " (" + task.getName() + ")");
        }
    }

    /**
     * Deletes the specified task from the database.
     *
     * @param task The task to be deleted.
     * @return A boolean value representing whether or not the task was deleted.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean deleteTask(Task task) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Task WHERE Task.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, task.getId());

            stmt.executeUpdate();

            return true;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Deleted task with ID: " + task.getId() + " (" + task.getName() + ")");
        }
    }

    /**
     * Saves the time having been worked on the task in the database.
     *
     * @param task The task being worked on.
     * @param time The total amount of time having been worked on the task in seconds.
     * @param startTime The starttime of when the work began.
     * @param stopTime The endtime of when the work ended.
     * @throws java.sql.SQLException
     */
    @Override
    public void addTimeForTask(Task task, int time, Date startTime, Date stopTime) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Tasks (taskID, time, startTime, stopTime) "
                    + "VALUES (?,?,?,?);";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, task.getId());
            stmt.setInt(2, time);
            stmt.setString(3, TimeConverter.convertDateToStringDB(startTime));
            stmt.setString(4, TimeConverter.convertDateToStringDB(stopTime));

            stmt.executeUpdate();

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Added time to task with ID: " + task.getId() + " (" + task.getName() + ") for " + TimeConverter.convertSecondsToString(time));
        }
    }

    /**
     * Returns the total amount of time having been used on the specified task in seconds.
     *
     * @param taskID The ID of the task.
     * @return An int value representing the total amount of time having been used on the task in seconds.
     * @throws java.sql.SQLException
     */
    @Override
    public int getTaskTime(int taskID) throws SQLException {

        Connection con = null;
        int totalTime = 0;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT SUM(Tasks.time) AS TotalTime FROM Tasks WHERE Tasks.TaskID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                totalTime = rs.getInt("TotalTime");
            }
            return totalTime;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Loads all time stored for the task.
     *
     * @param taskId The ID of the task being searched for.
     * @return An observablelist containing all time registered for the task.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<TaskTime> loadTimeForTask(int taskId) throws SQLException {

        Connection con = null;
        ObservableList<TaskTime> tasks = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Tasks WHERE Tasks.taskID = ? ORDER BY Tasks.startTime asc";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                int time = rs.getInt("time");
                Date startTime = TimeConverter.convertStringToDateDB(rs.getString("startTime"));
                Date stopTime = TimeConverter.convertStringToDateDB(rs.getString("stopTime"));

                TaskTime taskTime = new TaskTime(time, startTime, stopTime);
                taskTime.setId(id);
                tasks.add(taskTime);
            }
            return tasks;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param taskTime The taskTime that will update the previous taskTime with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean UpdateTimeForTask(TaskTime taskTime) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Tasks SET Tasks.time = ?, Tasks.startTime = ?, Tasks.stopTime = ? WHERE Tasks.ID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskTime.getTime());
            stmt.setString(2, TimeConverter.convertDateToStringDB(taskTime.getStartTime()));
            stmt.setString(3, TimeConverter.convertDateToStringDB(taskTime.getStopTime()));
            stmt.setInt(4, taskTime.getId());

            stmt.executeUpdate();

            return true;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated time for task time with ID: " + taskTime.getId() + " - " + TimeConverter.convertSecondsToString(taskTime.getTime()));
        }
    }

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param taskTime The taskTime to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean DeleteTimeForTask(TaskTime taskTime) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Tasks WHERE Tasks.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskTime.getId());

            stmt.executeUpdate();

            return true;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Deleted task time with ID: " + taskTime.getId());
        }
    }

}
