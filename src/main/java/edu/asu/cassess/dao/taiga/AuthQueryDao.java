package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.taiga.AuthUser;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class AuthQueryDao implements IAuthQueryDao {

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
    @Transactional
    public List<AuthUser> getUsers() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM cassess.taiga_user");
        List<AuthUser> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public AuthUser getUser(String username) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM cassess.taiga_user WHERE username = ?1", AuthUser.class);
        query.setParameter(1, username);
        return (AuthUser) query.getSingleResult();
    }

    @Override
    @Transactional
    public void removeDuplicateUser(Long id) throws DataAccessException {
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.taiga_user WHERE id = ?1");
            query.setParameter(1, id);
            query.executeUpdate();
    }
}
