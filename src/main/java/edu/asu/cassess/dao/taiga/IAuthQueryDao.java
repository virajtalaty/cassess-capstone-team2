package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.AuthUser;

import javax.persistence.EntityManager;
import java.util.List;

public interface IAuthQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<AuthUser> getUsers() throws DataAccessException;

    AuthUser getUser(String username) throws DataAccessException;

    void removeDuplicateUser(Long id) throws DataAccessException;
}
