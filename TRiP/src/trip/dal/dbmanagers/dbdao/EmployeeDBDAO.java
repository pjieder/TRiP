/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import attendanceautomation.dal.dbaccess.DBSettings;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Admin;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Roles;
import trip.be.Task;
import trip.be.TaskTime;
import trip.be.User;
import trip.utilities.HashAlgorithm;
import trip.utilities.TimeConverter;

/**
 *
 * @author ander
 */
public class EmployeeDBDAO implements IEmployeeDBDAO{

    
    //adds employee to Employees Table in SQL
    public boolean createEmployee(Employee employee)
    {
        Connection con = null;
        try
        {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Employees (fName, lName, email, isAdmin) "
                    + "VALUES (?,?,?,?);";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, employee.getfName());
            stmt.setString(2, employee.getlName());
            stmt.setString(3, employee.getEmail());
            
            if (employee.getRole() == Roles.ADMIN)
            {
                stmt.setBoolean(4, true);
            } else
            {
                stmt.setBoolean(4, false);
            }
   
            int updatedRows = stmt.executeUpdate();
            
           
            
            return updatedRows > 0;
        }
        catch (SQLServerException ex)
        {
            //TODO
        }
        catch (SQLException ex)
        {
            //TODO
        }
        finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }
    
    
    /**
     * Returns the ID of the user based on whether the login information given is valid or not.
     * @param userName The username of the account
     * @param password The password of the account
     * @return int value representing the ID of the user. If no user is found, an ID of -1 is returned.
     */
    @Override
    public int isLoginCorrect(String userName, String password) {
        
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Login WHERE username = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, userName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                
                String salt = rs.getString("salt");
                String hashedPassword = HashAlgorithm.generateHash(password, salt);
                
                if (hashedPassword.equals(rs.getString("hashedPassword")))
                {
                    return rs.getInt("employeeID");
                }
            } 

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return -1;
    }

    /**
     * Returns the role of the person containing a specific ID.
     * @param id The ID of the person being searched for
     * @return The role of the person entered
     */
    @Override
    public Roles getRoleById(int id) {
        
        Connection con = null;
        
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT isAdmin FROM Employees WHERE Employees.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                
                boolean isAdmin = rs.getBoolean("isAdmin");
                
                if (isAdmin == true) {
                    return Roles.ADMIN;
                } else {
                    return Roles.USER;
                }
            }
        } catch (SQLServerException ex) {
            
        } catch (SQLException ex) {
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        
        return null;
    }
    
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
    
     public ObservableList<Employee> loadEmployees() {

        Connection con = null;
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Employees;";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                String fname = rs.getString("fName");
                String lname = rs.getString("lName");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("isAdmin");

                Employee employee;
                
                if (isAdmin == true)
                {
                    employee = new Admin(fname, lname, email);
                }
                else
                {
                    employee = new User(fname, lname, email);
                }
                    employee.setId(id);
                    employees.add(employee);
            }

            return employees;

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return employees;
    }
    
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
     
      public ObservableList<TaskTime> loadTimeForTask(int taskId) {

       Connection con = null;
       ObservableList<TaskTime> tasks = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Tasks WHERE Tasks.taskID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                int time = rs.getInt("time");
                Date startTime = TimeConverter.convertStringToDate(rs.getString("startTime"));
                Date stopTime = TimeConverter.convertStringToDate(rs.getString("stopTime"));
                
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
