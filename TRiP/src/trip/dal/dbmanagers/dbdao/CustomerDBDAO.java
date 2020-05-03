/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Customer;
import trip.dal.dbaccess.DBSettings;
import trip.dal.dbmanagers.dbdao.Interfaces.ICustomerDBDAO;

/**
 *
 * @author Jacob
 */
public class CustomerDBDAO implements ICustomerDBDAO {

    private DBSettings dbCon;

    public CustomerDBDAO() {
        try {
            dbCon = new DBSettings();
        } catch (IOException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves the newly created customer in the database.
     *
     * @param customer The customer to be saved.
     */
    @Override
    public void createCustomer(Customer customer) {
        Connection con = null;
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
                customer.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Adds the customer to the project.
     *
     * @param customerID The ID of the customer.
     * @param projectID The ID of the project.
     */
    @Override
    public void addCustomerToProject(int customerID, int projectID) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO CustomerProjects (CustomerProjects.customerID, CustomerProjects.projectID) VALUES (?,?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setInt(2, projectID);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Loads all customers stored in the database.
     *
     * @return Returns an observablelist containing all the stored customers.
     */
    @Override
    public ObservableList<Customer> getAllCustomers() {
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
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return null;
    }

    /**
     * Returns the customer for the specified project.
     *
     * @param projectID The project ID of the specified project.
     * @return The customer on the project.
     */
    @Override
    public Customer getCustomerForProject(int projectID) {
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
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        return customer;
    }

    /**
     * Updates the specified customer in the database.
     *
     * @param customer The customer that will update the previous customer with the same ID.
     */
    @Override
    public void updateCustomer(Customer customer) {
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
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer The customer to be deleted.
     */
    @Override
    public void deleteCustomer(Customer customer) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM Customer WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }

    /**
     * Deletes the customer from the specified project.
     *
     * @param projectID The ID of the project that the costumer should be removed from.
     */
    @Override
    public void removeCustomerFromProject(int projectID) {
        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "DELETE FROM CustomerProjects WHERE CustomerProjects.projectID = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, projectID);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
    }
}
