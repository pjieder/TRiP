/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import javafx.collections.ObservableList;
import trip.be.Project;
import trip.dal.dbmanagers.facades.DalFacade;
import trip.dal.dbmanagers.facades.IDalFacade;

/**
 *
 * @author ander
 */
public class ProjectManager {

    private IDalFacade dalFacade;

    public ProjectManager() {
        dalFacade = new DalFacade();
    }

    public ObservableList<Project> loadAllProjects(int employeeId) {
        return dalFacade.loadAllProjects(employeeId);
    }

    public ObservableList<Project> loadUserProjects(int employeeId) {
        return dalFacade.loadUserProjects(employeeId);
    }

}
