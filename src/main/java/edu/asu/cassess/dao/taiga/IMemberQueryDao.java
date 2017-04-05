package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.MemberData;

import javax.persistence.EntityManager;
import java.util.List;

public interface IMemberQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);
    
    /**
     * Get members based on role and/or slug.
     * 
     * @param roleName the name of the role to filter by
     * @param project_slug the Taiga slug to filter by
     * @return List of MemberData
     * @throws DataAccessException
     */
    List<MemberData> getMembers(String roleName, String project_slug) throws DataAccessException;
}
