package com.cassess.dao.taiga;

import com.cassess.entity.taiga.Project;
import com.cassess.entity.taiga.ProjectIDSlug;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface IProjectQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<Project> getAllProjects() throws DataAccessException;

    Project getProject(String slug) throws DataAccessException;

    List<ProjectIDSlug> listGetProjectIDSlug(String course) throws DataAccessException;
}
