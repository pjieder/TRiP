/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import attendanceautomation.dal.dbaccess.DBSettings;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Project;
import trip.be.Task;
import trip.be.TaskTime;
import trip.be.User;

/**
 *
 * @author ander
 */
public class UserDBDAO {

    /**
     * Returns the user based on the specified ID.
     *
     * @param id the ID of the user
     * @return The user with the specified ID
     */
    public User getUserById(int id) {

        Connection con = null;
        User user = null;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM User WHERE User.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String fname = rs.getString("fName");
                String lname = rs.getString("lName");
                String email = rs.getString("email");

                user = new User(fname, lname, email);
                user.setId(id);

            }

            return user;

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return user;
    }

    public void loadProjects(int userID, ObservableList<Project> projects) {
        for (Project project : projects) {

            project.setTasks(getProjectTasks(userID, project.getId()));

            for (Task task : project.getTasks()) {
                task.setTasks(getTaskTimes(task.getId()));
            }
            
        }
    }

    public ObservableList<Task> getProjectTasks(int userId, int projectId) {

        Connection con = null;
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Task WHERE Task.userID = ? AND Task.projID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, userId);
            stmt.setInt(2, projectId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int taskID = rs.getInt("ID");
                String taskName = rs.getString("name");

                Task task = new Task(taskName);
                task.setId(taskID);
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

    public ObservableList<TaskTime> getTaskTimes(int taskId) {

        Connection con = null;
        ObservableList<TaskTime> taskTimes = FXCollections.observableArrayList();
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Tasks WHERE Task.taskID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String taskName = rs.getString("name");
                int time = rs.getInt("time");
                Date startTime = rs.getDate("startTime");
                Date endTime = rs.getDate("endTime");
                LocalDate date = LocalDate.parse(rs.getString("date"));

                TaskTime taskTime = new TaskTime(time, startTime, endTime, date);
                taskTime.setId(taskId);
                taskTimes.add(taskTime);

            }

            return taskTimes;

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return taskTimes;
    }

}
