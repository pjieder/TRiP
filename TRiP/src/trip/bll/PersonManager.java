/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import trip.be.Employee;
import trip.dal.dbmanagers.facades.DalFacade;
import trip.dal.dbmanagers.facades.IDalFacade;
import trip.utilities.HashAlgorithm;

/**
 *
 * @author ander
 */
public class PersonManager {
    
    private IDalFacade dalFacade;

    public PersonManager() {
        dalFacade = new DalFacade();
    }

    /**
     * Returns the person found by the entered username and password.
     * @param username The username of the person
     * @param password The password of the person
     * @return The person found based on the username and password
     */
    public Employee validateUser(String username, String password)
    {
       return dalFacade.login(username, password);
    }
    
    
}
