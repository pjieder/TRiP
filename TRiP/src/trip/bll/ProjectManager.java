/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.util.List;
import trip.be.Project;
import trip.dal.dbmanagers.dbdao.IProjectDBDAO;

/**
 *
 * @author Jacob
 */
public class ProjectManager
{
    private IProjectDBDAO projectDao;
    
    public List<Project> getAllProjects()
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
}
