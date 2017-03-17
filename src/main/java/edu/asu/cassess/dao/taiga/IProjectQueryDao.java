package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.Project;
import edu.asu.cassess.persist.entity.taiga.ProjectIDSlug;

import javax.persistence.EntityManager;
import java.util.List;

public interface IProjectQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<Project> getAllProjects() throws DataAccessException;

    Project getProject(String slug) throws DataAccessException;

    List<ProjectIDSlug> listGetProjectIDSlug(String course) throws DataAccessException;
}
