/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import trip.dal.dbmanagers.dbdao.Interfaces.IAdminDBDAO;
import trip.dal.dbaccess.DBSettings;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import trip.be.Admin;



/**
 *
 * @author ander
 */
public class AdminDBDAO implements IAdminDBDAO{
    
    /**
     * Returns the user based on the specified ID.
     *
     * @param id the ID of the user
     * @return The user with the specified ID
     */
    @Override
    public Admin getAdminById(int id) {

        Connection con = null;
        Admin admin = null;

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

                admin = new Admin(fname, lname, email);
                admin.setId(id);

            }

            return admin;

        } catch (SQLServerException ex) {

        } catch (SQLException ex) {

        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

        return admin;
    }
    
}
