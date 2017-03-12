package com.cassess.dao.taiga;

import com.cassess.entity.taiga.Project;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface IProjectQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<Project> getProjects() throws DataAccessException;

    Project getProject(String slug) throws DataAccessException;

}
