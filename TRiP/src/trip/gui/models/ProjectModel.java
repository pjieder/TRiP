/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.be.Project;
import trip.bll.ProjectManager;

/**
 *
 * @author Jacob
 */
public class ProjectModel
{
    private final ProjectManager projectManager;
    

    public ProjectModel()
    {
        projectManager = new ProjectManager();
    }

    public ObservableList<Project> getAllProjects()
    {
        return projectManager.getAllProjects();
    }
    
    public ObservableList<Project> loadAllActiveProjects(int employeeId)
    {
        return projectManager.loadAllActiveProjects(employeeId);
    }

    public ObservableList<Project> loadAllUserProjects(int employeeId)
    {
        return projectManager.loadUserProjects(employeeId);
    }

    public void createProject(Project project, List<Employee> allEmployees)
    {
        projectManager.createProject(project, allEmployees);
    }

    public void updateProject(Project project, List<Employee> allEmployees)
    {
        projectManager.updateProject(project, allEmployees);
    }

    public void deleteProject(Project project)
    {
        projectManager.deleteProject(project);
    }
}
