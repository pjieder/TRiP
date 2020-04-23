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
public class Project {

    private int id;
    private String name;
    private double rate;
    private boolean isActive;

    public Project(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }
    
    public Project()
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean getIsActive()
    {
        return isActive;
    }

    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object obj) {

        Project project = (Project) obj;

        if (project.getId() == this.getId()) {
            return true;
        }
        return false;

    }

}
