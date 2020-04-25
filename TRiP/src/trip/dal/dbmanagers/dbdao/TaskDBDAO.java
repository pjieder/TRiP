/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import com.microsoft.sqlserver.jdbc.SQLServerException;
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
import trip.be.Timer;
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

        } catch (SQLServerException ex) {
            //TODO
        } catch (SQLException ex) {
            //TODO
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return -1;
    }

    @Override
    public void addTimeToTask(int taskId, int time, Date startTime, Date stopTime) {
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

            ResultSet rs = stmt.executeQuery();

        } catch (SQLServerException ex) {
            //TODO
        } catch (SQLException ex) {
            //TODO
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

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
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

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return tasks;
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

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return tasks;
    }

}
