package com.cassess.dao.taiga;

import com.cassess.entity.taiga.AuthUser;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface IAuthQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<AuthUser> getUsers() throws DataAccessException;

    AuthUser getUser(String username) throws DataAccessException;

    void removeDuplicateUser(Long id) throws DataAccessException;
}
