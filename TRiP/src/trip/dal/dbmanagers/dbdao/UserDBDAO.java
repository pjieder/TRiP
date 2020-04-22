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
            String sql = "SELECT * FROM Employees WHERE Employees.ID = ?;";
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

    public ObservableList<Project> getEmployeeProjects(int employeeID) {

        Connection con = null;
        ObservableList<Project> projects = FXCollections.observableArrayList();
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Project JOIN Projects on Project.ID = Projects.projID WHERE Projects.employeeID = ? AND Project.isActive = 1;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, employeeID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int projectID = rs.getInt("ID");
                String projectName = rs.getString("name");
                double rate = rs.getDouble("rate");

                Project project = new Project(projectName, rate);
                project.setId(projectID);
                projects.add(project);

            }

            return projects;

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return projects;
    }

}
