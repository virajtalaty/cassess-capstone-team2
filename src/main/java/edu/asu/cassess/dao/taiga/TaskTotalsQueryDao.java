package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.model.Taiga.DisplayAllTasks;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.cassess.persist.entity.taiga.TaskTotals;
import edu.asu.cassess.persist.entity.taiga.WeeklyTotals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@Transactional
public class TaskTotalsQueryDao implements ITaskTotalsQueryDao {

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
    public List<TaskTotals> getTaskTotals() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM cassess.tasktotals", TaskTotals.class);
        List<TaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<DisplayAllTasks> getTaskTotals(String name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate, tasksClosed, tasksOpen, tasksInProgress, tasksReadyForTest, tasksNew FROM cassess.tasktotals WHERE fullName = ?1", DisplayAllTasks.class);
        query.setParameter(1, name);
        List<DisplayAllTasks> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<WeeklyTotals> getWeeklyTasks(String fullName) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT fullName, WeekEnding, COALESCE(ClosedTasks, 0) AS ClosedTasks, COALESCE(OpenTasks, 0) AS OpenTasks FROM (SELECT tasktotals1.fullName, tasktotals1.retrievalDate AS WeekEnding, tasktotals1.tasksClosed - tasktotals2.tasksClosed as ClosedTasks, IF(tasktotals1.tasksClosed - tasktotals2.tasksClosed >= 0, (tasktotals1.tasksOpen - tasktotals2.tasksOpen) + (tasktotals1.tasksClosed - tasktotals2.tasksClosed), tasktotals1.tasksOpen - tasktotals2.tasksOpen) as OpenTasks FROM tasktotals tasktotals1 LEFT JOIN tasktotals tasktotals2 ON tasktotals1.id=tasktotals2.id AND tasktotals2.retrievalDate = (SELECT MAX(tasktotals3.retrievalDate) FROM tasktotals tasktotals3 WHERE tasktotals3.retrievalDate < tasktotals1.retrievalDate AND tasktotals3.id = tasktotals1.id) WHERE tasktotals1.fullName = ?1 ) AS WeeklyTotals", WeeklyTotals.class);
        query.setParameter(1, fullName);
        List<WeeklyTotals> resultList = query.getResultList();
        System.out.print(resultList);
        return resultList;
    }

}
