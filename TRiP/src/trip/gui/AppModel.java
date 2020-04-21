/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui;

import trip.be.Employee;
import trip.bll.PersonManager;

/**
 *
 * @author ander
 */
public class AppModel {

    private final PersonManager personManager;

    public AppModel() {
        personManager = new PersonManager();
    }
    
    /**
     * If correct username and password is entered,
     * the stored person will be returned.
     * @param username the username of the account
     * @param password the password of the account
     * @return 
     */
    public Employee validateUser(String username, String password) {
        return personManager.validateUser(username, password);
    }

}
