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
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.CountedTime;
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
                    + "VALUES (?,?,?,?);";
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
     * Loads all unique tasks having been worked on between two specified dates within the specified project.
     * @param projectID The id of the project searching tasks for.
     * @param startDate The startdate of the time span.
     * @param endDate The enddate of the time span.
     * @return An observablelist containing all the stored tasks searched for.
     * @throws SQLException 
     */
    @Override
    public ObservableList<Task> loadAllUniqueTasksDates(int projectID, LocalDate startDate, LocalDate endDate) throws SQLException
    {
        Connection con = null;
        ObservableList<Task> tasks = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            
            String sql = "WITH x AS (SELECT Tasks.time, Task.* FROM Tasks JOIN Task ON Tasks.taskID = Task.ID WHERE Tasks.startTime BETWEEN ? AND ? AND Task.projID = ?)" +
            "SELECT DISTINCT x.ID, x.name, Employees.fName, Employees.lName, COALESCE((SELECT SUM (Tasks.time) FROM Tasks WHERE Tasks.taskID = x.ID AND Tasks.isBillable = 0),0) as unbillabletime, SUM(x.time) over (partition by x.ID) as totalTime FROM x JOIN Employees ON x.employeeID = Employees.ID;";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, startDate.toString());
            stmt.setString(2, TimeConverter.addDays(endDate, 1).toString());
            stmt.setInt(3, projectID);
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                String name = rs.getString("name");
                String employeeName = rs.getString("fName") + " " + rs.getString("lName");
                int totalTime = rs.getInt("totalTime");
                int unbillableTime = rs.getInt("unbillableTime");

                Task task = new Task(name);
                task.setId(id);
                task.setEmployee(employeeName);
                task.setTotalTime(totalTime);
                task.setUnbillabletime(unbillableTime);
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
        int ID = -1;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Tasks (taskID, time, startTime, stopTime, isBillable) "
                    + "VALUES (?,?,?,?,?);";

            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, task.getId());
            stmt.setInt(2, time);
            stmt.setString(3, TimeConverter.convertDateToStringDB(startTime));
            stmt.setString(4, TimeConverter.convertDateToStringDB(stopTime));
            stmt.setBoolean(5, true);

             stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                ID = generatedKeys.getInt(1);
            }

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Created counted time with ID: " + ID);
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
    public int getCountedTime(int taskID) throws SQLException {

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
    public ObservableList<CountedTime> loadTimeForTask(int taskId) throws SQLException {

        Connection con = null;
        ObservableList<CountedTime> tasks = FXCollections.observableArrayList();

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
                boolean isBillable = rs.getBoolean("isBillable");

                CountedTime countedTime = new CountedTime(time, startTime, stopTime);
                countedTime.setId(id);
                countedTime.setBillable(isBillable);
                tasks.add(countedTime);
            }
            return tasks;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Updates the specified time having been worked on the task in the database.
     *
     * @param countedTime The counted time that will update the previous counted time with the same ID.
     * @return A boolean value representing whether or not the update was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updateTimeForTask(CountedTime countedTime) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Tasks SET Tasks.time = ?, Tasks.startTime = ?, Tasks.stopTime = ?, Tasks.isBillable = ? WHERE Tasks.ID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, countedTime.getTime());
            stmt.setString(2, TimeConverter.convertDateToStringDB(countedTime.getStartTime()));
            stmt.setString(3, TimeConverter.convertDateToStringDB(countedTime.getStopTime()));
            stmt.setBoolean(4, countedTime.isBillable());
            stmt.setInt(5, countedTime.getId());


            stmt.executeUpdate();

            return true;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated time for counted time with ID: " + countedTime.getId() + " - " + TimeConverter.convertSecondsToString(countedTime.getTime()));
        }
    }

    /**
     * Deletes the specified time registered to the task in the database.
     *
     * @param countedTime The counted time to be deleted.
     * @return A boolean value representing whether or not the delete was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean deleteTimeForTask(CountedTime countedTime) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Tasks WHERE Tasks.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, countedTime.getId());

            stmt.executeUpdate();

            return true;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Deleted counted time with ID: " + countedTime.getId());
        }
    }

}
