package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.persist.entity.taiga.DisplayAllTasks;
import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.TaskTotals;
import edu.asu.cassess.persist.entity.taiga.WeeklyTotals;

import javax.persistence.EntityManager;
import java.util.List;

public interface ITaskTotalsQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    /**
     * Get a list of all Taiga task totals from database.
     * 
     * @return List of TaskTotals
     * @throws DataAccessException
     */
    List<TaskTotals> getTaskTotals() throws DataAccessException;

    /**
     * Get task totals from database for the given name.
     * 
     * @param name the fullname of the Taiga user
     * @return List of DisplayAllTasks
     * @throws DataAccessException
     */
    List<DisplayAllTasks> getTaskTotals(String name) throws DataAccessException;

    /**
     * Get task totals for this week based on the given name.
     * 
     * @param fullName the fullname of the Taiga user
     * @return List of WeeklyTotals
     * @throws DataAccessException
     */
    List<WeeklyTotals> getWeeklyTasks(String fullName) throws DataAccessException;
}
