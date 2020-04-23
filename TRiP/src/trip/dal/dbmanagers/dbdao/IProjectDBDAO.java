/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import java.util.List;
import trip.be.Project;

/**
 *
 * @author Jacob
 */
public interface IProjectDBDAO
{
    public List<Project> getAllProjects();
    
    public void createProject(Project project);
    
    public void updateProject(Project project);
    
    public void deleteProject(Project project);
}
