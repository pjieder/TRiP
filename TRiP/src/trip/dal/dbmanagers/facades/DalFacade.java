/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.facades;

import trip.be.Person;
import trip.be.Roles;
import trip.dal.dbmanagers.dbdao.IPersonDBDAO;
import trip.dal.dbmanagers.dbdao.PersonDBDAO;

/**
 *
 * @author ander
 */
public class DalFacade implements IDalFacade{

    private IPersonDBDAO personManager = new PersonDBDAO();
    
    @Override
    public Person login(String username, String password) {
        
        int userId = personManager.isLoginCorrect(username, password);

        if (userId != -1) {
            Roles role = personManager.getRoleById(userId);

        }

        return null;
    }
    
}
