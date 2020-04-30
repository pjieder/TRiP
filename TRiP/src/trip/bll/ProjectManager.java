/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.bll;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import trip.be.Employee;
import trip.be.Project;
import trip.dal.dbmanagers.facades.IDalFacade;
import trip.dal.dbmanagers.facades.DalFacade;
import trip.utilities.TimeConverter;

/**
 *
 * @author Jacob
 */
public class ProjectManager {

    private IDalFacade dalFacade;

    public ProjectManager() {
        dalFacade = new DalFacade();
    }

    public void createProject(Project project, List<Employee> allEmployees) {
        dalFacade.createProject(project, allEmployees);
    }

    public ObservableList<Project> getAllProjects() {
        return dalFacade.getAllProjects();
    }

    public ObservableList<Project> loadAllActiveProjects() {
        return dalFacade.loadAllActiveProjects();
    }

    public ObservableList<Project> loadAllInactiveProjects() {
        return dalFacade.loadAllInactiveProjects();
    }

    public ObservableList<Project> loadEmployeeProjects(int employeeId) {
        return dalFacade.loadEmployeeProjects(employeeId);
    }

    public void updateProject(Project project, List<Employee> allEmployees) {
        dalFacade.updateProject(project, allEmployees);
    }

    public void deleteProject(Project project) {
        dalFacade.deleteProject(project);
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
            double value = (double) time.get(i) / 3600;
            series.getData().add(new XYChart.Data<>(datesX.get(i).toString(), value));
        }

        return series;
    }

}
