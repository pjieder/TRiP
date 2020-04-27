/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.util.List;
import javafx.collections.ObservableList;
import trip.be.Employee;
import trip.be.Project;
import trip.dal.dbmanagers.dbdao.Interfaces.IProjectDBDAO;
import trip.dal.dbmanagers.dbdao.ProjectDBDAO;
import trip.dal.dbmanagers.facades.IDalFacade;
import trip.dal.dbmanagers.facades.DalFacade;

/**
 *
 * @author Jacob
 */
public class ProjectManager
{
    private IProjectDBDAO projectDao;
    private IDalFacade dalFacade;
    
    public ProjectManager() {
        projectDao = new ProjectDBDAO();
        dalFacade = new DalFacade();
    }
    
    public ObservableList<Project> getAllProjects()
    {
        return projectDao.getAllProjects();
    }
    
    public void createProject(Project project, List<Employee> allEmployees)
    {
        dalFacade.createProject(project, allEmployees);
    }
    
    public void updateProject(Project project, List<Employee> allEmployees)
    {
        dalFacade.updateProject(project, allEmployees);
    }
    
    public void deleteProject(Project project)
    {
        projectDao.deleteProject(project);
    }

    public ObservableList<Project> loadAllActiveProjects(int employeeId) {
        return dalFacade.loadAllActiveProjects(employeeId);
    }

    public ObservableList<Project> loadUserProjects(int employeeId) {
        return dalFacade.loadUserProjects(employeeId);
    }
}
