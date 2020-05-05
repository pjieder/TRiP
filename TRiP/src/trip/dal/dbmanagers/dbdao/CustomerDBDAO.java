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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Customer;
import trip.be.Project;
import trip.dal.dbaccess.DBSettings;
import trip.dal.dbmanagers.dbdao.Interfaces.ICustomerDBDAO;
import trip.dal.dbmanagers.dbdao.utilities.DatabaseLogger;

/**
 *
 * @author Jacob
 */
public class CustomerDBDAO implements ICustomerDBDAO {

    /**
     * Saves the newly created customer in the database.
     *
     * @param customer The customer to be saved.
     * @throws java.sql.SQLException
     */
    @Override
    public void createCustomer(Customer customer) throws SQLException {
        Connection con = null;
        int ID = 0;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Customer (name, phoneNumber, email) VALUES (?,?,?);";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());

            int affectedRows = ps.executeUpdate();
            if (affectedRows < 1) {
                throw new SQLException();
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ID = rs.getInt(1);
            }
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Created customer with ID: " + ID + "(" + customer.getName() + ")");
        }
    }

    /**
     * Adds the customer to the project.
     *
     * @param customer The customer to be added.
     * @param project The project to be added.
     * @throws java.sql.SQLException
     */
    @Override
    public void addCustomerToProject(Customer customer, Project project) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO CustomerProjects (CustomerProjects.customerID, CustomerProjects.projectID) VALUES (?,?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.setInt(2, project.getId());
            ps.execute();
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Added customer with ID" + customer.getId() + " (" + customer.getName() + ")" + " to project with ID " + project.getId() + " (" + project.getName() + ")");
        }
    }

    /**
     * Loads all customers stored in the database.
     *
     * @return Returns an observablelist containing all the stored customers.
     * @throws java.sql.SQLException
     */
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        Connection con = null;
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * From Customer;";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                customer.setEmail(rs.getString("email"));

                allCustomers.add(customer);
            }
            return allCustomers;
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Returns the customer for the specified project.
     *
     * @param projectID The project ID of the specified project.
     * @return The customer on the project.
     * @throws java.sql.SQLException
     */
    @Override
    public Customer getCustomerForProject(int projectID) throws SQLException {
        Connection con = null;
        Customer customer = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "SELECT * FROM Customer JOIN CustomerProjects on Customer.ID = CustomerProjects.customerID WHERE "
                    + "CustomerProjects.projectID = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, projectID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                customer.setEmail(rs.getString("email"));
            }
            return customer;
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Updates the specified customer in the database.
     *
     * @param customer The customer that will update the previous customer with the same ID.
     * @throws java.sql.SQLException
     */
    @Override
    public void updateCustomer(Customer customer) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "UPDATE Customer SET name=?, phoneNumber=?, email=? WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setInt(4, customer.getId());

            int affected = ps.executeUpdate();
            if (affected < 1) {
                throw new SQLException();
            }
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Updated customer with ID: " + customer.getId() + " (" + customer.getName() + ")");
        }
    }

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer The customer to be deleted.
     * @throws java.sql.SQLException
     */
    @Override
    public void deleteCustomer(Customer customer) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Customer WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.execute();
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Deleted customer with ID: " + customer.getId() + " (" + customer.getName() + ")");
        }
    }

    /**
     * Deletes the customer from the specified project.
     *
     * @param projectID The ID of the project that the costumer should be removed from.
     * @throws java.sql.SQLException
     */
    @Override
    public void removeCustomerFromProject(int projectID) throws SQLException {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM CustomerProjects WHERE CustomerProjects.projectID = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, projectID);
            ps.execute();
        } finally {
            DBSettings.getInstance().releaseConnection(con);
            DatabaseLogger.logAction("Removed all customers from project with ID: " + projectID);
        }
    }
}
