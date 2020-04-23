/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import trip.be.User;

/**
 *
 * @author ander
 */
public interface IUserDBDAO {
    
    public User getUserById(int id);
    
    
    
}
