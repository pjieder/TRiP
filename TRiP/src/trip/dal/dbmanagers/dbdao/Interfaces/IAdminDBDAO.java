/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import java.sql.SQLException;
import trip.be.Admin;

/**
 *
 * @author ander
 */
public interface IAdminDBDAO {

    /**
     * Returns the adnun based on the specified ID.
     *
     * @param id the ID of the admin
     * @return The admin with the specified ID
     * @throws java.sql.SQLException
     */
    public Admin getAdminById(int id) throws SQLException;

}
