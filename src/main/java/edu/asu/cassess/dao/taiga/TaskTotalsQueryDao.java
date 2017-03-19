package edu.asu.cassess.dao.taiga;

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
    public List<TaskTotals> getTaskTotals(String name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM cassess.tasktotals WHERE fullName = ?1", TaskTotals.class);
        query.setParameter(1, name);
        List<TaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<WeeklyTotals> getWeeklyTasks(String fullName) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT tasktotals.id, tasktotals.fullName, (SELECT MIN(tasktotalsmin.retrievalDate) FROM tasktotals AS tasktotalsmin WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate AND tasktotals.id = tasktotalsmin.id ) AS WeekEnding, (SELECT GREATEST(MIN(tasktotalsmin.tasksClosed) - tasktotals.tasksClosed, 0) FROM tasktotals AS tasktotalsmin WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate AND tasktotals.id = tasktotalsmin.id ) AS ClosedTasks, ( SELECT GREATEST(MIN(tasktotalsmin.tasksOpen) - tasktotals.tasksOpen, 0) FROM tasktotals AS tasktotalsmin WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate AND tasktotals.id = tasktotalsmin.id ) AS OpenTasks, ( SELECT GREATEST(MIN(tasktotalsmin.tasksNew) - tasktotals.tasksNew, 0) FROM tasktotals AS tasktotalsmin WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate AND tasktotals.id = tasktotalsmin.id ) AS NewTasks, ( SELECT GREATEST(MIN(tasktotalsmin.tasksInProgress) - tasktotals.tasksInProgress, 0) FROM tasktotals AS tasktotalsmin WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate AND tasktotals.id = tasktotalsmin.id ) AS InProgressTasks, ( SELECT GREATEST(MIN(tasktotalsmin.tasksReadyForTest) - tasktotals.tasksReadyForTest, 0) FROM tasktotals AS tasktotalsmin WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate AND tasktotals.id = tasktotalsmin.id ) ReadyForTestTasks FROM tasktotals WHERE fullName = ?1", WeeklyTotals.class);
        query.setParameter(1, fullName);
        List<WeeklyTotals> resultList = query.getResultList();
        System.out.print(resultList);
        return resultList;
    }

}
