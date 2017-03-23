package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.cassess.persist.entity.taiga.Project;
import edu.asu.cassess.persist.entity.taiga.ProjectIDSlug;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@Transactional
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
    public List<Project> getAllProjects() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.project");
        List<Project> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public Project getProject(String slug) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.project WHERE slug = ?1", Project.class);
        query.setParameter(1, slug);
        return (Project) query.getSingleResult();
    }

    @Override
    public List<ProjectIDSlug> listGetProjectIDSlug(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT id, slug FROM cassess.project INNER JOIN cassess.students ON cassess.project.slug=cassess.students.taiga_project_slug AND course=?1", ProjectIDSlug.class);
        query.setParameter(1, course);
        List<ProjectIDSlug> resultList = query.getResultList();
        return resultList;
    }

}
