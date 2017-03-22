package edu.asu.cassess.dao;

import edu.asu.cassess.persist.entity.UserID;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface IUserQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    UserID getUserID() throws DataAccessException;
}
