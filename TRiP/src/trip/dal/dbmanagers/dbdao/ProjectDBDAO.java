/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trip.dal.dbmanagers.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import trip.be.Project;
import trip.dal.dbaccess.DBSettings;

/**
 *
 * @author Jacob
 */
public class ProjectDBDAO implements IProjectDBDAO
{
    private final DBSettings dbCon;

    public ProjectDBDAO() throws Exception
    {
        dbCon = new DBSettings();
    }
    
    @Override
    public List<Project> getAllProjects()
    {
        ArrayList<Project> allProjects = new ArrayList<>();
        try ( Connection con = dbCon.getConnection())
        {
            String sql = "SELECT * From Project;";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setRate(rs.getDouble("rate"));
                project.setIsActive(rs.getBoolean("isActive"));
                
                allProjects.add(project);
            }
            return allProjects;
        } catch (SQLException ex)
        {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void createProject(Project project)
    {
        try ( Connection con = dbCon.getConnection())
        {
            String sql = "INSERT INTO Project (name, rate, isActive) VALUES (?,?,?);";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, project.getName());
            ps.setDouble(2, project.getRate());
            ps.setBoolean(3, project.getIsActive());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows < 1)
            {
                throw new SQLException();
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
            {
                project.setId(rs.getInt(1));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateProject(Project project)
    {
        try ( Connection con = dbCon.getConnection())
        {
            String sql = "UPDATE Project SET name=?, rate=?, isActive=? WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, project.getName());
            ps.setDouble(2, project.getRate());
            ps.setBoolean(3, project.getIsActive());
            ps.setInt(4, project.getId());
            
            int affected = ps.executeUpdate();
            if (affected < 1)
            {
                throw new SQLException();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteProject(Project project)
    {
        try ( Connection con = dbCon.getConnection())
        {
            String sql = "DELETE FROM Project WHERE id=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, project.getId());
            ps.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(ProjectDBDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
