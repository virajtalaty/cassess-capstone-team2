package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.rest.RestResponse;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;

public interface ITaskQueryDao {
    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    RestResponse truncateTaskData();

    /**
     * Get count of Tasks with status Closed based on Taiga user.
     *
     * @param full_name the username to filter tasks by
     * @return int value representing the number of Closed tasks
     * @throws DataAccessException
     */
    int getClosedTasks(String full_name) throws DataAccessException;

    /**
     * Get count of Tasks with status New based on Taiga user.
     *
     * @param full_name the username to filter tasks by
     * @return int value representing the number of New tasks
     * @throws DataAccessException
     */
    int getNewTasks(String full_name) throws DataAccessException;

    /**
     * Get count of Tasks with status In Progress based on Taiga user.
     *
     * @param full_name the username to filter tasks by
     * @return int value representing the number of In Progress tasks
     * @throws DataAccessException
     */
    int getInProgressTasks(String full_name) throws DataAccessException;

    /**
     * Get count of Tasks with status Ready For Test based on Taiga user.
     *
     * @param full_name the username to filter tasks by
     * @return int value representing the number of Ready For Test tasks
     * @throws DataAccessException
     */
    int getReadyForTestTasks(String full_name) throws DataAccessException;
}
