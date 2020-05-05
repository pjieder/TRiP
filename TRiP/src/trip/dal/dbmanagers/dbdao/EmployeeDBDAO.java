/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import trip.dal.dbmanagers.dbdao.Interfaces.IEmployeeDBDAO;
import trip.dal.dbaccess.DBSettings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Admin;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Roles;
import trip.be.User;
import trip.dal.dbmanagers.dbdao.utilities.DatabaseLogger;
import trip.utilities.HashAlgorithm;

/**
 *
 * @author ander
 */
public class EmployeeDBDAO implements IEmployeeDBDAO {

    /**
     * Returns the ID of the user based on whether the login information given is valid or not.
     *
     * @param userName The username of the account
     * @param password The password of the account
     * @return int value representing the ID of the user. If no user is found, an ID of -1 is returned.
     * @throws java.sql.SQLException
     */
    @Override
    public int isLoginCorrect(String userName, String password) throws SQLException {

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

                if (hashedPassword.equals(rs.getString("hashedPassword"))) {
                    return rs.getInt("employeeID");
                }
            }

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return -1;
    }

    /**
     * Saves the newly created employee in the database.
     *
     * @param employee The employee to be saved.
     * @param password The desired password for the newly created employee.
     * @return Boolean value representing whether or not the creation was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean createEmployee(Employee employee, String password) throws SQLException {
        Connection con = null;
        int ID = 0;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Employees (fName, lName, email, isAdmin, isActive) "
                    + "VALUES (?,?,?,?,?);";
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, employee.getfName());
            stmt.setString(2, employee.getlName());
            stmt.setString(3, employee.getEmail());
            if (employee.getRole() == Roles.ADMIN) {
                stmt.setBoolean(4, true);
            } else {
                stmt.setBoolean(4, false);
            }
            stmt.setBoolean(5, true);
            int updatedRows = stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                ID = generatedKeys.getInt(1);
                createPassword(employee, password, ID);
            }

            return updatedRows > 0;
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Created employee with ID: " + ID + " (" + employee.getfName() + ")");
        }
    }

    /**
     * Hashes and salts the password entered by the user and saves it to the database.
     *
     * @param employee The employee to be saved.ed username for the employee.
     * @param password The entered password for the employee.
     * @param ID The ID of the employee.
     * @throws java.sql.SQLException
     */
    @Override
    public void createPassword(Employee employee, String password, int ID) throws SQLException {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Login (employeeID, username, hashedPassword, salt) "
                    + "VALUES (?,?,?,?);";
            PreparedStatement stmt = con.prepareStatement(sql);

            String salt = HashAlgorithm.generateSalt();
            String hashedPassword = HashAlgorithm.generateHash(password, salt);
            stmt.setInt(1, ID);
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, salt);

            int updatedRows = stmt.executeUpdate();

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Created password for employee with ID: " + ID + " (" + employee.getfName() + ")");
        }
    }

    /**
     * Adds the employee to the project.
     *
     * @param employee The employee to be added.
     * @param project The project the employee should be added to.
     * @throws java.sql.SQLException
     */
    @Override
    public void addEmployeeToProject(Employee employee, Project project) throws SQLException {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Projects (employeeID, projID) VALUES (?,?);";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, employee.getId());
            stmt.setInt(2, project.getId());

            int updatedRows = stmt.executeUpdate();

        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Added employee with ID" + employee.getId() + " (" + employee.getfName() + ")" + " to project with ID " + project.getId() + " (" + project.getName() + ")");
        }
    }

    /**
     * Loads all active employees
     *
     * @return Returns an observablelist containing all active employees stored.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadActiveEmployees() throws SQLException {

        Connection con = null;
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Employees WHERE Employees.isActive = 1 ORDER BY Employees.fName ASC;";
            PreparedStatement stmt = con.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                String fname = rs.getString("fName");
                String lname = rs.getString("lName");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("isAdmin");

                Employee employee;

                if (isAdmin == true) {
                    employee = new Admin(fname, lname, email);
                } else {
                    employee = new User(fname, lname, email);
                }
                employee.setId(id);
                employees.add(employee);
            }

            return employees;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Loads all inactive employees
     *
     * @return Returns an observablelist containing all inactive employees stored.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadInactiveEmployees() throws SQLException {

        Connection con = null;
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Employees WHERE Employees.isActive = 0 ORDER BY Employees.fName ASC;";
            PreparedStatement stmt = con.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                String fname = rs.getString("fName");
                String lname = rs.getString("lName");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("isAdmin");

                Employee employee;

                if (isAdmin == true) {
                    employee = new Admin(fname, lname, email);
                } else {
                    employee = new User(fname, lname, email);
                }
                employee.setId(id);
                employees.add(employee);
            }

            return employees;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @return Returns an observablelist containing all employees assigned to the specified project.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) throws SQLException {

        Connection con = null;
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Employees JOIN Projects on Projects.employeeID = Employees.ID WHERE Projects.projID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, projectId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                String fname = rs.getString("fName");
                String lname = rs.getString("lName");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("isAdmin");
                boolean isActive = rs.getBoolean("isActive");

                Employee employee;

                if (isAdmin == true) {
                    employee = new Admin(fname, lname, email);
                } else {
                    employee = new User(fname, lname, email);
                }
                employee.setId(id);
                employee.setIsActive(isActive);
                employees.add(employee);
            }

            return employees;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Loads all employees assigned to the specified project.
     *
     * @param projectId The ID of the project searching for.
     * @param isActive Boolean value representing whether or not the employees should be active or inactive
     * @return Returns an observablelist containing all active or inactive employees assigned to the specified project.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) throws SQLException {

        Connection con = null;
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Employees JOIN Projects on Projects.employeeID = Employees.ID WHERE Projects.projID = ? AND Employees.isActive = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, projectId);
            stmt.setBoolean(2, isActive);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("ID");
                String fname = rs.getString("fName");
                String lname = rs.getString("lName");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("isAdmin");

                Employee employee;

                if (isAdmin == true) {
                    employee = new Admin(fname, lname, email);
                } else {
                    employee = new User(fname, lname, email);
                }
                employee.setId(id);
                employees.add(employee);
            }

            return employees;

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Returns the role of the person containing a specific ID.
     *
     * @param id The ID of the person being searched for
     * @return The role of the person entered
     * @throws java.sql.SQLException
     */
    @Override
    public Roles getRoleById(int id) throws SQLException {

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
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return null;
    }

    /**
     * Updates the specified employee in the database.
     *
     * @param employee The employee that will update the previous employee with the same ID.
     * @return Boolean value representing whether or not update was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean updateEmployee(Employee employee) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Employees SET fName = ?, lName = ?, email = ?, isAdmin = ? WHERE id = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, employee.getfName());
            stmt.setString(2, employee.getlName());
            stmt.setString(3, employee.getEmail());
            if (employee.getRole() == Roles.ADMIN) {
                stmt.setBoolean(4, true);
            } else {
                stmt.setBoolean(4, false);
            }
            stmt.setInt(5, employee.getId());

            int updatedRows = stmt.executeUpdate();
            updateUsername(employee.getEmail(), employee);
            
            return updatedRows > 0;
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated employee with ID: " + employee.getId() + " (" + employee.getfName() + ")");
        }
    }

    /**
     * Updates whether or not the specified employee is active.
     *
     * @param employee The employee to be updated.
     * @param active Boolean representing whether or not the user should be active or inactive.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateEmployeeActive(Employee employee, boolean active) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Employees SET isActive = ? WHERE ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setBoolean(1, active);
            stmt.setInt(2, employee.getId());

            stmt.executeUpdate();

        }finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated employee with ID: " + employee.getId() + " (" + employee.getfName() + ") to active: " + active);
        }
    }

    /**
     * Updates the username of the employee.
     *
     * @param username The new username wished for the employee.
     * @param employee The employee to be updated.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateUsername(String username, Employee employee) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Login SET username = ? WHERE employeeID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setInt(2, employee.getId());

            int updatedRows = stmt.executeUpdate();

        }finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated username of employee with ID: " + employee.getId() + " (" + employee.getfName() + ")" + " to " + username);
        }
    }

    /**
     * Updates the password of the specified employee.
     *
     * @param password The new password to be hashed and stored.
     * @param employee The employee to be updated.
     * @throws java.sql.SQLException
     */
    @Override
    public void updatePassword(String password, Employee employee) throws SQLException {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Login SET hashedPassword = ?, salt = ? WHERE employeeID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            String salt = HashAlgorithm.generateSalt();
            String hashedPassword = HashAlgorithm.generateHash(password, salt);

            stmt.setString(1, hashedPassword);
            stmt.setString(2, salt);
            stmt.setInt(3, employee.getId());

            int updatedRows = stmt.executeUpdate();

        }finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated password of employee with ID: " + employee.getId() + " (" + employee.getfName() + ")");
        }
    }

    /**
     * Deletes the specified employee from the database.
     *
     * @param employee The employee to be deleted.
     * @return Boolean representing whether or not the update was successful.
     * @throws java.sql.SQLException
     */
    @Override
    public boolean deleteEmployee(Employee employee) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Employees WHERE ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, employee.getId());

            int updatedRows = stmt.executeUpdate();

            return updatedRows > 0;
        }finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Deleted employee with ID: " + employee.getId() + " (" + employee.getfName() + ")");
        }
    }

    /**
     * Removes all employees working on the project.
     *
     * @param project The project all users should be removed from.
     * @throws java.sql.SQLException
     */
    @Override
    public void removeAllEmployeesFromProject(Project project) throws SQLException {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Projects WHERE Projects.projID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, project.getId());

            int updatedRows = stmt.executeUpdate();

        }finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Removed all employees from project with ID: " + project.getId() + " (" + project.getName() + ")");
        }
    }

}
