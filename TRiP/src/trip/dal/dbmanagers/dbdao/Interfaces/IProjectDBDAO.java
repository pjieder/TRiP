/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao.Interfaces;

import java.time.LocalDate;
import javafx.collections.ObservableList;
import trip.be.Project;

/**
 *
 * @author Jacob
 */
public interface IProjectDBDAO {

    public void createProject(Project project);

    public ObservableList<Project> loadAllProjects();

    public ObservableList<Project> loadAllActiveProjects();

    public ObservableList<Project> loadAllInactiveProjects();

    public ObservableList<Project> loadEmployeeProjects(int employeeID);

    public int loadProjectTime(int employeeID, int projectID);

    public int loadTotalProjectTime(int projectID);

    public int loadAllProjectTimeForDay(LocalDate date);

    public int loadProjectTimeForDay(int projectID, LocalDate date);

    public void updateProject(Project project);

    public void deleteProject(Project project);

}
