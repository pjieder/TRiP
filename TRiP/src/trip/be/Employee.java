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
public abstract class Employee {

    private int id;
    private Roles role;
    private String fName;
    private String lName;
    private String email;
    private ObservableList<Project> projects = FXCollections.observableArrayList();

    public Employee(String fName, String lName, String email, Roles role) {
        this.fName = fName;
        this.lName = lName;
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

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ObservableList<Project> projects) {
        this.projects = projects;
    }

    @Override
    public boolean equals(Object obj) {

        Employee person = (Employee) obj;

        if (person.getId() == this.getId()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return fName + " " + lName;
    }

}
