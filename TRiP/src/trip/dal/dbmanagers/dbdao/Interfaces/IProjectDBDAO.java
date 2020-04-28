/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import javafx.collections.ObservableList;
import trip.be.Project;

/**
 *
 * @author Jacob
 */
public interface IProjectDBDAO
{
    public ObservableList<Project> getAllProjects();
    
    public void createProject(Project project);
    
    public void updateProject(Project project);
    
    public void deleteProject(Project project);
    
    public ObservableList<Project> getAllActiveProjects();
    
    public ObservableList<Project> getAllInactiveProjects();
    
    public ObservableList<Project> getEmployeeProjects(int employeeID);
    
    public int getProjectTime(int employeeID, int projectID);
    
    public int getTotalProjectTime(int projectID);
}
