/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.gui.models;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import trip.be.Employee;
import trip.be.Project;
import trip.bll.ProjectManager;

/**
 *
 * @author Jacob
 */
public class ProjectModel {

    private final ProjectManager projectManager;

    public ProjectModel() {
        projectManager = new ProjectManager();
    }

    public ObservableList<Project> getAllProjects() {
        return projectManager.getAllProjects();
    }

    public ObservableList<Project> loadAllActiveProjects() {
        return projectManager.loadAllActiveProjects();
    }

    public ObservableList<Project> loadAllInactiveProjects() {
        return projectManager.loadAllInactiveProjects();
    }

    public ObservableList<Project> loadAllUserProjects(int employeeId) {
        return projectManager.loadEmployeeProjects(employeeId);
    }

    public void createProject(Project project, List<Employee> allEmployees) {
        projectManager.createProject(project, allEmployees);
    }

    public void updateProject(Project project, List<Employee> allEmployees) {
        projectManager.updateProject(project, allEmployees);
    }

    public void deleteProject(Project project) {
        projectManager.deleteProject(project);
    }

    public ObservableList<Project> searchProjects(String projectName, ObservableList<Project> projectList) {
        return projectManager.searchProjects(projectName, projectList);
    }
    
     public XYChart.Series calculateGraph(int projectID, LocalDate date1, LocalDate date2) {
         return projectManager.calculateGraph(projectID, date1, date2);
    }
}
