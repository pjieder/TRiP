/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.be;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ander
 */
public class User extends Employee{
    
    public User(String fName, String lName, String email) {
        super(fName, lName, email, Roles.USER);
    }
    
    
}
