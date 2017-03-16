package com.cassess.dao.taiga;

import com.cassess.entity.taiga.Project;
import com.cassess.entity.taiga.ProjectIDSlug;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class ProjectQueryDao implements IProjectQueryDao {

    protected EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<Project> getAllProjects() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.project");
        List<Project> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public Project getProject(String slug) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.project WHERE slug = ?1", Project.class);
        query.setParameter(1, slug);
        return (Project) query.getSingleResult();
    }

    @Override
    @Transactional
    public List<ProjectIDSlug> listGetProjectIDSlug(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT id, slug FROM cassess.project INNER JOIN cassess.students ON cassess.project.slug=cassess.students.taiga_project_slug AND course=?1", ProjectIDSlug.class);
        query.setParameter(1, course);
        List<ProjectIDSlug> resultList = query.getResultList();
        return resultList;
    }

}
