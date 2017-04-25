package edu.asu.cassess.dao.slack;

import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Thomas on 4/22/2017.
 */
public interface IUserObjectQueryDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    <T> Object getUserByEmail(String email) throws DataAccessException;
}
