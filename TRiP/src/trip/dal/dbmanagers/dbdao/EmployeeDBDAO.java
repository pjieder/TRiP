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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Admin;
import trip.be.Employee;
import trip.be.Roles;
import trip.be.User;
import trip.utilities.HashAlgorithm;

/**
 *
 * @author ander
 */
public class EmployeeDBDAO implements IEmployeeDBDAO {

    /**
     * adds employee to Employees Table in SQL
     *
     * @param employee The employee to add
     * @param password the password wanted stored
     * @return
     */
    @Override
    public boolean createEmployee(Employee employee, String password) {
        Connection con = null;
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
                createPassword(employee.getEmail(), password, generatedKeys.getInt(1));

            }

            return updatedRows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
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

            return updatedRows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }

    @Override
    public boolean deleteEmployee(Employee employee) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Employees WHERE ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, employee.getId());

            int updatedRows = stmt.executeUpdate();

            return updatedRows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return false;
    }

    @Override
    public void createPassword(String username, String password, int ID) {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Login (employeeID, username, hashedPassword, salt) "
                    + "VALUES (?,?,?,?);";
            PreparedStatement stmt = con.prepareStatement(sql);

            String salt = HashAlgorithm.generateSalt();
            String hashedPassword = HashAlgorithm.generateHash(password, salt);
            stmt.setInt(1, ID);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, salt);

            int updatedRows = stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

    }

    @Override
    public void updatePassword(String username, String password, int ID) {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Login SET username = ?, hashedPassword = ?, salt = ? WHERE employeeID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            String salt = HashAlgorithm.generateSalt();
            String hashedPassword = HashAlgorithm.generateHash(password, salt);

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, salt);
            stmt.setInt(4, ID);

            int updatedRows = stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

    }

    /**
     * Returns the ID of the user based on whether the login information given is valid or not.
     *
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

                if (hashedPassword.equals(rs.getString("hashedPassword"))) {
                    return rs.getInt("employeeID");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return -1;
    }

    /**
     * Returns the role of the person containing a specific ID.
     *
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
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return null;
    }

    @Override
    public ObservableList<Employee> loadActiveUsers() {

        Connection con = null;
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Employees WHERE Employees.isActive = 1;";
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

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return employees;
    }

    @Override
    public void updateEmployeeActive(int employeeId, boolean active) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Employees SET isActive = ? WHERE ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setBoolean(1, active);
            stmt.setInt(2, employeeId);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    @Override
    public ObservableList<Employee> loadInactiveUsers() {

        Connection con = null;
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Employees WHERE Employees.isActive = 0;";
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

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return employees;
    }

    @Override
    public void addEmployeeToProject(int employeeID, int projID) {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Projects (employeeID, projID) VALUES (?,?);";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, employeeID);
            stmt.setInt(2, projID);

            int updatedRows = stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    @Override
    public void removeAllEmployeesFromProject(int projID) {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Projects WHERE Projects.projID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, projID);

            int updatedRows = stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId) {

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

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return employees;
    }

    @Override
    public ObservableList<Employee> loadEmployeesAssignedToProject(int projectId, boolean isActive) {

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

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return employees;
    }

}
