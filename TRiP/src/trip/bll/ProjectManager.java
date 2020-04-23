/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.util.List;
import javafx.collections.ObservableList;
import trip.be.Project;
import trip.dal.dbmanagers.dbdao.Interfaces.IProjectDBDAO;
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
        dalFacade = new DalFacade();
    }
    
    public ObservableList<Project> getAllProjects()
    {
        return projectDao.getAllProjects();
    }
    
    public void createProject(Project project)
    {
        projectDao.createProject(project);
    }
    
    public void updateProject(Project project)
    {
        projectDao.updateProject(project);
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
