/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import trip.be.Employee;
import trip.be.Project;
import trip.dal.dbmanagers.dbdao.Interfaces.IProjectDBDAO;
import trip.dal.dbmanagers.dbdao.ProjectDBDAO;
import trip.dal.dbmanagers.facades.IDalFacade;
import trip.dal.dbmanagers.facades.DalFacade;
import trip.utilities.TimeConverter;

/**
 *
 * @author Jacob
 */
public class ProjectManager {

    private IProjectDBDAO projectDao;
    private IDalFacade dalFacade;

    public ProjectManager() {
        projectDao = new ProjectDBDAO();
        dalFacade = new DalFacade();
    }

    public ObservableList<Project> getAllProjects() {
        return projectDao.getAllProjects();
    }

    public void createProject(Project project, List<Employee> allEmployees) {
        dalFacade.createProject(project, allEmployees);
    }

    public void updateProject(Project project, List<Employee> allEmployees) {
        dalFacade.updateProject(project, allEmployees);
    }

    public void deleteProject(Project project) {
        projectDao.deleteProject(project);
    }

    public ObservableList<Project> loadAllActiveProjects() {
        return dalFacade.loadAllActiveProjects();
    }

    public ObservableList<Project> loadAllInactiveProjects() {
        return dalFacade.loadAllInactiveProjects();
    }

    public ObservableList<Project> loadUserProjects(int employeeId) {
        return dalFacade.loadUserProjects(employeeId);
    }

    public ObservableList<Project> searchProjects(String projectName, ObservableList<Project> projectList) {

        ObservableList<Project> searchProjectList = FXCollections.observableArrayList();

        for (Project project : projectList) {

            if (project.getName().toLowerCase().contains(projectName.toLowerCase())) {

                searchProjectList.add(project);
            }
        }
        return searchProjectList;
    }

    public XYChart.Series calculateGraph(int projectID, LocalDate dateStart, LocalDate dateEnd) {

        List<LocalDate> datesX = TimeConverter.getDaysBetweenDates(dateStart, dateEnd);
        List<Integer> time = dalFacade.loadTimeForDates(projectID, datesX);

        XYChart.Series series = new XYChart.Series();
        
        for (int i = 0; i < datesX.size(); i++) {
            double value = (double) time.get(i)/3600;
            series.getData().add(new XYChart.Data<>(datesX.get(i).toString(), value));
        }
        
        return series;
    }

    

    public static void main(String[] args) {

        ProjectManager manager = new ProjectManager();
        LocalDate d1 = LocalDate.now();
        LocalDate d2 = TimeConverter.addDays(d1, -5);
        
        List<LocalDate> dates = TimeConverter.getDaysBetweenDates(d1, d2);
        System.out.println(dates.size());

//        manager.calculateGraph(d1, d2);

    }

}
