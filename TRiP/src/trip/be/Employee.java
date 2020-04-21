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
public abstract class Employee {

    private int id;
    private Roles role;
    private String name;
    private String email;

    public Employee(String name, String email, Roles role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public boolean equals(Object obj) {
      
        Employee person = (Employee) obj;

        if (person.getId() == this.getId()) {
            return true;
        }
        return false;

    }

}
