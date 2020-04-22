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
public enum Roles {
    
        USER("user"),ADMIN("admin");
    
    private String type;

    private Roles(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
}
