package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Team;
import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.Project;
import edu.asu.cassess.persist.entity.taiga.ProjectIDSlug;

import javax.persistence.EntityManager;
import java.util.List;

public interface IProjectQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    /**
     * Get a list of all projects from database.
     * 
     * @return List of Projects
     * @throws DataAccessException
     */
    List<Project> getAllTaigaProjects() throws DataAccessException;

    /**
     * Get Project data from database for this slug; only ever returns one object.
     * 
     * @param slug the Taiga slug to filter by
     * @return a single Project object
     * @throws DataAccessException
     */
    Project getTaigaProject(String slug) throws DataAccessException;

    /**
     * Get Project ID Slug(s) from database given this course.
     * 
     * @param course the name of the course to filter by
     * @return List of ProjectIDSlug object(s)
     * @throws DataAccessException
     */
    List<ProjectIDSlug> listGetTaigaProjectIDSlug(String course) throws DataAccessException;

    RestResponse deleteTaigaProjectByCourse(Course course);

    RestResponse deleteTaigaProjectByTeam(Team team);
}
