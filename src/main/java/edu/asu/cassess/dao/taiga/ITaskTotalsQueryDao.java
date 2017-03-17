package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.TaskTotals;
import edu.asu.cassess.persist.entity.taiga.WeeklyTotals;

import javax.persistence.EntityManager;
import java.util.List;

public interface ITaskTotalsQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<TaskTotals> getTaskTotals() throws DataAccessException;

    List<TaskTotals> getTaskTotals(String name) throws DataAccessException;

    List<WeeklyTotals> getWeeklyTasks(String fullName) throws DataAccessException;
}
