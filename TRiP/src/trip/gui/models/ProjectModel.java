/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trip.be.Project;
import trip.bll.ProjectManager;

/**
 *
 * @author Jacob
 */
public class ProjectModel
{

    private static ProjectModel instance;
    private final ProjectManager projectManager;
//    private final ObservableList<Project> allProjects;
    private final ObservableList<Project> selectedProject;

    public ProjectModel()
    {
        this.projectManager = new ProjectManager();
//        allProjects = FXCollections.observableArrayList();
//        allProjects.addAll(projectManager.getAllProjects());
        selectedProject = FXCollections.observableArrayList();
    }

    public static ProjectModel getInstance()
    {
        if (instance == null)
        {
            instance = new ProjectModel();
        }
        return instance;
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

    public void createProject(Project project)
    {
        projectManager.createProject(project);
    }

    public void updateProject(Project project)
    {
        projectManager.updateProject(project);
//        allProjects.add(project);
//        allProjects.clear();
//        allProjects.addAll(projectManager.getAllProjects());
    }

    public void deleteProject(Project project)
    {
        projectManager.deleteProject(project);
//        allProjects.remove(project);
    }
}
