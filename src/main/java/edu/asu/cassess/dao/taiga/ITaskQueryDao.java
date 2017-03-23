package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;

public interface ITaskQueryDao {
    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    int getClosedTasks(String full_name) throws DataAccessException;

    int getNewTasks(String full_name) throws DataAccessException;

    int getInProgressTasks(String full_name) throws DataAccessException;

    int getReadyForTestTasks(String full_name) throws DataAccessException;
}
