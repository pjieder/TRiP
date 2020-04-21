/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.facades;

import trip.be.Admin;
import trip.be.Employee;
import trip.be.Roles;
import trip.be.User;
import trip.dal.dbmanagers.dbdao.IPersonDBDAO;
import trip.dal.dbmanagers.dbdao.PersonDBDAO;
import trip.dal.dbmanagers.dbdao.UserDBDAO;

/**
 *
 * @author ander
 */
public class DalFacade implements IDalFacade {

    private PersonDBDAO personManager = new PersonDBDAO();
    private UserDBDAO userManager = new UserDBDAO();

    @Override
    public Employee login(String username, String password) {

        int employeeId = personManager.isLoginCorrect(username, password);

        if (employeeId != -1) {
            Roles role = personManager.getRoleById(employeeId);

            if (role == Roles.USER) {
                User user = userManager.getUserById(employeeId);
                user.setProjects(personManager.getAllProjects());
                userManager.loadProjects(employeeId, user.getProjects());
                return userManager.getUserById(employeeId);

            } else if (role == Roles.ADMIN) {
                Admin admin = teacherManager.getTeacherById(userId);

                for (Classroom c : teacher.getClassrooms()) {
                    c.setStudents(studentManager.getStudentsInClass(c));
                }

                return teacher;
            }

        }

        return null;
    }

}
