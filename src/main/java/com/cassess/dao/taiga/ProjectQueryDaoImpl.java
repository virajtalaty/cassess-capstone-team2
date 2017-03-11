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
        Query query = getEntityManager().createNativeQuery("SELECT * FROM project");
        List<Project> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public Project getProject(String slug) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM project WHERE slug = ?1", Project.class);
        query.setParameter(1, slug);
        return (Project) query.getSingleResult();
    }
}
