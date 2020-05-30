/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.be;

/**
 *
 * @author ander
 */
public class Admin extends Employee{
    
    public Admin(String fName, String lName, String email) {
        super(fName, lName, email, Roles.ADMIN);
    }
    
    public static void main(String[] args) {
        int[] test = {1,2,3};
        
        System.out.println(test[1]);
        System.out.println(test[1] * 2);
        test[1] = test[1]*3;
        System.out.println(test[1]);
    }
    
}
