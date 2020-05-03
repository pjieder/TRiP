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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Task;
import trip.be.TaskTime;
import trip.dal.dbaccess.DBSettings;
import trip.dal.dbmanagers.dbdao.Interfaces.ITaskDBDAO;
import trip.utilities.TimeConverter;

/**
 *
 * @author ander
 */
public class TaskDBDAO implements ITaskDBDAO {

    @Override
    public int addTask(int userId, int projectId, String taskName) {
        Connection con = null;
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
                return generatedKeys.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return -1;
    }

    @Override
    public ObservableList<Task> loadTasks(int userId, int projectId) {

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

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return tasks;
    }

    @Override
    public boolean updateTask(Task task) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Task SET Task.name = ? WHERE Task.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, task.getName());
            stmt.setInt(2, task.getId());

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }

    @Override
    public boolean deleteTask(int taskId) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Task WHERE Task.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskId);

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }

    @Override
    public void addTimeForTask(int taskId, int time, Date startTime, Date stopTime) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Tasks (taskID, time, startTime, stopTime) "
                    + "VALUES (?,?,?,?);";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskId);
            stmt.setInt(2, time);
            stmt.setString(3, TimeConverter.convertDateToStringDB(startTime));
            stmt.setString(4, TimeConverter.convertDateToStringDB(stopTime));

            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    @Override
    public int getTaskTime(int taskID) {

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

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
    }

    @Override
    public ObservableList<TaskTime> loadTimeForTask(int taskId) {

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

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return tasks;
    }

    @Override
    public boolean UpdateTimeForTask(TaskTime taskTime) {
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

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }

    @Override
    public boolean DeleteTimeForTask(TaskTime taskTime) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Tasks WHERE Tasks.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskTime.getId());

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(TaskDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }

}
