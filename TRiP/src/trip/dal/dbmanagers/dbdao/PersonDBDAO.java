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
import trip.be.Roles;
import trip.utilities.HashAlgorithm;

/**
 *
 * @author ander
 */
public class PersonDBDAO implements IPersonDBDAO{

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
            String sql = "SELECT * FROM Logins WHERE username = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, userName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                
                String salt = rs.getString("salt");
                String hashedPassword = HashAlgorithm.generateHash(password, salt);
                
                if (hashedPassword.equals(rs.getString("password")))
                {
                    return rs.getInt("persID");
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
            String sql = "SELECT isStudent FROM Person WHERE Person.ID = ?;";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                
                boolean isStudent = rs.getBoolean("isStudent");
                
                if (isStudent == true) {
//                    return Roles.STUDENT;
                } else {
                    
//                    return Roles.TEACHER;
                }
            }
        } catch (SQLServerException ex) {
            
        } catch (SQLException ex) {
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }
        
        return null;
    }
    
}
