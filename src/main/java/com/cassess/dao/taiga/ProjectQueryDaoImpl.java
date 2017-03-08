package com.cassess.dao.taiga;

import com.cassess.entity.taiga.Project;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class ProjectQueryDaoImpl implements ProjectQueryDao {

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List<Project> getProjects() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM projects");
        List<Project> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public Project getProject() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM projects", Project.class);
        return (Project) query.getSingleResult();
    }
}
