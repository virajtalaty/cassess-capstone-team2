package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.slack.UserObject;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Thomas on 4/22/2017.
 */
public interface IUserObjectQueryDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    <T> Object getUserByEmail(String email) throws DataAccessException;

    <T> List<UserObject> getUsersByEmail(String email) throws DataAccessException;
}
