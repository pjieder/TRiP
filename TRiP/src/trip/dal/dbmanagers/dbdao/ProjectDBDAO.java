/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import trip.dal.dbmanagers.dbdao.Interfaces.IProjectDBDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import trip.be.Project;
import trip.dal.dbaccess.DBSettings;
import trip.dal.dbmanagers.dbdao.utilities.DatabaseLogger;
import trip.utilities.TimeConverter;

/**
 *
 * @author Jacob
 */
public class ProjectDBDAO implements IProjectDBDAO {

    private DBSettings dbCon;

    public ProjectDBDAO() {
        try {
            dbCon = new DBSettings();
        } catch (IOException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves the newly created project in the database.
     *
     * @param project The project to be saved.
     */
    @Override
    public void createProject(Project project) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Project (name, rate, isActive) VALUES (?,?,?);";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, project.getName());
            ps.setDouble(2, project.getRate());
            ps.setBoolean(3, project.getIsActive());

            int affectedRows = ps.executeUpdate();
            if (affectedRows < 1) {
                throw new SQLException();
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                project.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Created project with ID: " + project.getId() + " (" + project.getName() + ")");
        }
    }

    /**
     * Loads all saved projects in the database.
     *
     * @return An observablelist containing all projects stored.
     */
    @Override
    public ObservableList<Project> loadAllProjects() {
        Connection con = null;
        ObservableList<Project> allProjects = FXCollections.observableArrayList();
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * From Project;";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setRate(rs.getDouble("rate"));
                project.setIsActive(rs.getBoolean("isActive"));

                allProjects.add(project);
            }
            return allProjects;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return null;
    }

    /**
     * Loads all active projects stored in the database.
     *
     * @return An observablelist containing all active projects stored.
     */
    @Override
    public ObservableList<Project> loadAllActiveProjects() {
        Connection con = null;
        ObservableList<Project> projects = FXCollections.observableArrayList();
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Project WHERE isActive = 1;";
            PreparedStatement stmt = con.prepareStatement(sql);
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

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return projects;
    }

    /**
     * Loads all inactiev projects stored in the database.
     *
     * @return An observablelist containing all the inactive projects stored.
     */
    @Override
    public ObservableList<Project> loadAllInactiveProjects() {
        Connection con = null;
        ObservableList<Project> projects = FXCollections.observableArrayList();
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Project WHERE isActive = 0;";
            PreparedStatement stmt = con.prepareStatement(sql);
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

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return projects;
    }

    /**
     * Loads all stored projects assigned to the specified employee.
     *
     * @param employeeID The ID of the employee.
     * @return Returns an observablelist containing all projects assigned to the specified employee.
     */
    @Override
    public ObservableList<Project> loadEmployeeProjects(int employeeID) {

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

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return projects;
    }

    /**
     * Loads the total amount of time worked on the project by the specified employee.
     *
     * @param employeeID The ID of the employee working on the project.
     * @param projectID The ID of the project being worked on.
     * @return An int value representing the total amount of time worked on the project by the specified user in seconds.
     */
    @Override
    public int loadProjectTime(int employeeID, int projectID) {

        Connection con = null;
        int totalTime = 0;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT SUM(Tasks.time) AS TotalTime FROM Tasks JOIN Task on Tasks.taskID = Task.ID WHERE Task.projID = ? AND Task.employeeID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, projectID);
            stmt.setInt(2, employeeID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalTime = rs.getInt("TotalTime");
            }
            return totalTime;

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
    }

    /**
     * Loads the total amount of time having been used on the project by all users.
     *
     * @param projectID The ID of the project.
     * @return An int value representing the total amount of time worked on the project in seconds.
     */
    @Override
    public int loadTotalProjectTime(int projectID) {
        Connection con = null;
        int totalTime = 0;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT SUM(Tasks.time) AS TotalTime FROM Tasks JOIN Task on Tasks.taskID = Task.ID WHERE Task.projID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, projectID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                totalTime = rs.getInt("TotalTime");
            }
            return totalTime;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
    }

    /**
     * Loads the total amount of time having been registered on the specified project between a specified timespan.
     *
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @return An int value representing the total amount of time having been used in seconds.
     */
    @Override
    public int loadAllProjectTimeBetweenDates(int projectID, LocalDate startDate, LocalDate endDate) {
        Connection con = null;
        int totalTime = 0;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT SUM(Tasks.time) AS TotalTime FROM Tasks JOIN Task on Tasks.taskID = Task.ID WHERE Task.projID = ?  AND Tasks.startTime BETWEEN ? AND ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, projectID);
            stmt.setString(2, startDate.toString());
            stmt.setString(3, TimeConverter.addDays(endDate, 1).toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalTime = rs.getInt("TotalTime");
            }

            return totalTime;

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
    }

    /**
     * Loads the total amount of time having been registered on the specifiec project by the specified employee between the timespan.
     *
     * @param employeeID The ID of the specified employee .
     * @param projectID The ID of the project that the count is based upon.
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @return An int value representing the total amount of time the specified employe have been working on the project in seconds.
     */
    @Override
    public int loadAllEmployeeProjectTimeBetweenDates(int employeeID, int projectID, LocalDate startDate, LocalDate endDate) {
        Connection con = null;
        int totalTime = 0;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT SUM(Tasks.time) AS TotalTime FROM Tasks JOIN Task on Tasks.taskID = Task.ID WHERE Task.projID = ? And Task.employeeID = ? AND Tasks.startTime BETWEEN ? AND ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, projectID);
            stmt.setInt(2, employeeID);
            stmt.setString(3, startDate.toString());
            stmt.setString(4, TimeConverter.addDays(endDate, 1).toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalTime = rs.getInt("TotalTime");
            }

            return totalTime;

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
    }

    /**
     * Loads the total amount of time having been registered on the specified date.
     *
     * @param date The date being searched for.
     * @return An int value representing the total amount of work done on the specified date in seconds.
     */
    @Override
    public int loadAllProjectTimeForDay(LocalDate date) {

        Connection con = null;
        int totalTime = 0;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT SUM(Tasks.time) AS TotalTime FROM Tasks WHERE Tasks.startTime like ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, date.toString() + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalTime = rs.getInt("TotalTime");
            }

            return totalTime;

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
    }

    /**
     * Loads the total amount of time having been registered on the specified project on the date.
     *
     * @param projectID The project ID of the project being searched for.
     * @param date The date being searched for.
     * @return An int value representing the total amount of work done on the specified project on the specified date in seconds.
     */
    @Override
    public int loadProjectTimeForDay(int projectID, LocalDate date) {

        Connection con = null;
        int totalTime = 0;

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT SUM(Tasks.time) AS TotalTime FROM Tasks JOIN Task on Tasks.taskID = Task.ID WHERE Task.projID = ?  AND Tasks.startTime like ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, projectID);
            stmt.setString(2, date.toString() + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalTime = rs.getInt("TotalTime");
            }

            return totalTime;

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return totalTime;
    }

    /**
     * Loads all projects having been worked on between the specified dates by the specified employee.
     *
     * @param startDate The startdate of the timespan.
     * @param endDate The enddate of the timespan.
     * @param employeeID The ID of the specified employee.
     * @return A list containing all projects having been worked on by the specified employee between the two dates.
     */
    @Override
    public List<Project> loadWorkedOnProjectsBetweenDates(LocalDate startDate, LocalDate endDate, int employeeID) {
        Connection con = null;
        List<Project> allWorkedOnProjects = new ArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "WITH x AS (SELECT Tasks.*, Task.employeeID, Task.projID FROM Tasks JOIN Task ON Tasks.taskID = Task.ID WHERE Tasks.startTime BETWEEN ? AND ? AND Task.employeeID = ?)"
                    + "SELECT DISTINCT Project.* FROM x JOIN Project ON x.projID = Project.ID;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, startDate.toString());
            stmt.setString(2, TimeConverter.addDays(endDate, 1).toString());
            stmt.setInt(3, employeeID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("ID"));
                project.setName(rs.getString("name"));
                project.setRate(rs.getDouble("rate"));
                project.setIsActive(rs.getBoolean("isActive"));

                allWorkedOnProjects.add(project);
            }

            return allWorkedOnProjects;

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return allWorkedOnProjects;
    }

    /**
     * Calculates the chart series for the bargraph representing the total amount of time individual users have been working and on what tasks between the specified days.
     *
     * @param startDate The startdate of the timeframe.
     * @param endDate The enddate of the timeframe.
     * @param employeeID The Id of the employee that the series is based upon.
     * @return A series containing all the stored data to be displayed.
     */
    @Override
    public XYChart.Series loadTimeForUsersProjects(LocalDate startDate, LocalDate endDate, int employeeID) {

        Connection con = null;
        XYChart.Series series = new XYChart.Series();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "WITH x AS (SELECT Tasks.*, Task.employeeID, Task.projID FROM Tasks JOIN Task ON Tasks.taskID = Task.ID WHERE Tasks.startTime BETWEEN ? AND ? AND Task.employeeID = ?)"
                    + "SELECT DISTINCT x.employeeID, Project.*, SUM(x.time) over (partition by projID) as totalTime FROM x JOIN Project ON x.projID = Project.ID;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, startDate.toString());
            stmt.setString(2, TimeConverter.addDays(endDate, 1).toString());
            stmt.setInt(3, employeeID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int totalTime = rs.getInt("totalTime");
                String projectName = rs.getString("name");
                double value = (double) totalTime / 3600;

                series.getData().add(new XYChart.Data<>(projectName, value));
            }

            return series;

        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return series;
    }

    /**
     * Updates the specified project in the database.
     *
     * @param project The project that will update the previous project with the same ID.
     */
    @Override
    public void updateProject(Project project) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Project SET name=?, rate=?, isActive=? WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, project.getName());
            ps.setDouble(2, project.getRate());
            ps.setBoolean(3, project.getIsActive());
            ps.setInt(4, project.getId());

            int affected = ps.executeUpdate();
            if (affected < 1) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated project with ID: " + project.getId() + " (" + project.getName() + ")");
        }
    }

    /**
     * Deletes the specified project from the database.
     *
     * @param project The project to be deleted.
     */
    @Override
    public void deleteProject(Project project) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Project WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, project.getId());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Deleted project with ID: " + project.getId() + " (" + project.getName() + ")");
        }
    }

}
