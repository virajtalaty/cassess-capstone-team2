package com.cassess.model.taiga;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class AuthUserQueryDaoImpl implements AuthUserQueryDao {

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List<AuthUser> getUsers() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM taiga_user");
        List<AuthUser> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public AuthUser getUser(String username) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM taiga_user WHERE username = ?1", AuthUser.class);
        query.setParameter(1, username);
        return (AuthUser) query.getSingleResult();
    }
    
    @Transactional
    public void removeDuplicateUser(Long id) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("DELETE FROM taiga_user WHERE id = ?1");
        query.setParameter(1, id);
        query.executeUpdate();
    }
}
