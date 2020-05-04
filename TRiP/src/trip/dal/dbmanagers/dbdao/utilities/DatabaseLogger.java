/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import trip.dal.dbaccess.DBSettings;
import trip.dal.dbmanagers.dbdao.CustomerDBDAO;
import trip.gui.controllers.LoginController;

/**
 *
 * @author ander
 */
public class DatabaseLogger {

    public static void logAction(String logAction) {

        Connection con = null;
        try {
            con = DBSettings.getInstance().getConnection();
            String sql = "INSERT INTO Log (Employee, Action, Date) VALUES (?,?,?);";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, LoginController.loggedUser.getId() + " (" + LoginController.loggedUser.getfName() + ")");
            ps.setString(2, logAction);
            ps.setString(3, new Date().toString());
            
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(CustomerDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBSettings.getInstance().releaseConnection(con);
        }

    }

}
