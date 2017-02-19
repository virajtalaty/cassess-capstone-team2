package com.cassess.service.DAO;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cassess.entity.slack.UserObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class SlackServiceDAOImpl implements SlackServiceDAO {

    private EntityManager entityManager;

    private EntityManager getEntityManager() {
        return entityManager;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List<UserObject> getUsers() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM slack_user");
        List<UserObject> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public UserObject getUser(String id) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM slack_user WHERE id = ?1", UserObject.class);
        query.setParameter(1, id);
        return (UserObject) query.getSingleResult();
    }

}
