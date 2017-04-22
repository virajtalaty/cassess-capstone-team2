package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.rest.RestResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public interface ISlackMessageQueryDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    RestResponse truncateMessageData();

    int getMessageCount(String user) throws DataAccessException;
}
