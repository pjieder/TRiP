/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.facades;

import javafx.collections.ObservableList;
import trip.be.Admin;
import trip.be.Employee;
import trip.be.Project;
import trip.be.Roles;
import trip.be.User;
import trip.dal.dbmanagers.dbdao.AdminDBDAO;
import trip.dal.dbmanagers.dbdao.IPersonDBDAO;
import trip.dal.dbmanagers.dbdao.EmployeeDBDAO;
import trip.dal.dbmanagers.dbdao.UserDBDAO;

/**
 *
 * @author ander
 */
public class DalFacade implements IDalFacade {

    private EmployeeDBDAO employeeManager = new EmployeeDBDAO();
    private UserDBDAO userManager = new UserDBDAO();
    private AdminDBDAO adminManager = new AdminDBDAO();

    @Override
    public Employee login(String username, String password) {

        Employee employee = null;
        int employeeId = employeeManager.isLoginCorrect(username, password);

        if (employeeId != -1) {
            Roles role = employeeManager.getRoleById(employeeId);

            if (role == Roles.USER) {
                employee = userManager.getUserById(employeeId);
                employee.setProjects(userManager.getEmployeeProjects(employeeId));
            } else if (role == Roles.ADMIN) {
                employee = adminManager.getAdminById(employeeId);
                employee.setProjects(employeeManager.getAllActiveProjects());
            }

            for (Project project : employee.getProjects()) {
                project.setTotalTime(employeeManager.getProjectTime(employeeId, project.getId()));
            }

            return employee;

        }

        return employee;
    }


}
