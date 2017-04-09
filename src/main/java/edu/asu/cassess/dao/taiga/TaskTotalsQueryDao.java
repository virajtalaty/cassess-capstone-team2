package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.model.Taiga.DailyTaskTotals;
import edu.asu.cassess.model.Taiga.DisplayAllTasks;
import edu.asu.cassess.model.Taiga.WeeklyIntervals;
import edu.asu.cassess.model.Taiga.WeeklyUpdateActivity;
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
    public List<DailyTaskTotals> getDailyTasksByProject(String beginDate, String endDate, String project){
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', SUM(tasksInProgress) as 'InProgress', SUM(tasksReadyForTest) as 'ToTest', SUM(tasksClosed) as 'Done' FROM Cassess.tasktotals WHERE retrievalDate >= ?1 AND retrievalDate <= ?2 AND project = ?3 GROUP BY retrievalDate", DailyTaskTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, project);
        List<DailyTaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<DailyTaskTotals> getDailyTasksByStudent(String beginDate, String endDate, String project, String student){
        Query query = getEntityManager().createNativeQuery("SELECT retrievalDate as'Date', tasksInProgress as 'InProgress', tasksReadyForTest as 'ToTest', tasksClosed as 'Done' FROM Cassess.tasktotals WHERE retrievalDate >= ?1 AND retrievalDate <= ?2 AND project = ?3 AND email = ?4 GROUP BY retrievalDate", DailyTaskTotals.class);
        query.setParameter(1, beginDate);
        query.setParameter(2, endDate);
        query.setParameter(3, project);
        query.setParameter(4, student);
        List<DailyTaskTotals> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List<WeeklyIntervals> getWeeklyIntervalsByStudent(String project, String student){
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (1 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.tasktotals WHERE project = ?1 AND email = ?2 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
                query.setParameter(1, project);
                query.setParameter(2, student);
                List<WeeklyIntervals> resultList = query.getResultList();
                return resultList;
    }

    @Override
    public List<WeeklyIntervals> getWeeklyIntervalsByProject(String project){
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding FROM (SELECT DATE(retrievalDate + INTERVAL (1 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding' FROM cassess.tasktotals WHERE project = ?1 group by week(retrievalDate)) w1, (select @rn \\:= 0) vars", WeeklyIntervals.class);
                query.setParameter(1, project);
                List<WeeklyIntervals> resultList = query.getResultList();
                return resultList;
    }

    @Override
    public List<WeeklyUpdateActivity> getWeeklyUpdatesByProject(String project){
        Query query = getEntityManager().createNativeQuery("SELECT TSKF.week, TSKF.weekBeginning, TSKF.weekEnding, \n" +
                "\tTRIM(TRAILING '.' FROM (TRIM(TRAILING '0' FROM (TSKF.DoneActivity - @lastDoneActivity)))) as 'DoneActivity',\n" +
                "      @lastDoneActivity \\:= TSKF.DoneActivity,\n" +
                "\tTSKF.InProgressActivity,\n" +
                "\tTSKF.ToTestActivity\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, SUM(DoneActivity) as 'DoneActivity', SUM(InProgressActivity) as 'InProgressActivity', SUM(ToTestActivity) as 'ToTestActivity'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (1 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', tasksClosed as 'DoneActivity', SUM(tasksInProgressDIFF) as 'InProgressActivity', SUM(tasksReadyForTestDIFF) as 'ToTestActivity'\n" +
                "FROM\n" +
                "(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.project,\n" +
                "      TSK.fullName,\n" +
                "      TSK.tasksClosed as 'tasksClosed', \n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE project = ?1\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "      GROUP BY weekBeginning) TSKF, \n" +
                "      ( select @lastDoneActivity \\:= 0) SQLVars\n" +
                "      ", WeeklyUpdateActivity.class);
                query.setParameter(1, project);
                List<WeeklyUpdateActivity> resultList = query.getResultList();
                return resultList;
    }

    @Override
    public List<WeeklyUpdateActivity> getWeeklyUpdatesByStudent(String project, String student){
        Query query = getEntityManager().createNativeQuery("SELECT TSKF.week, TSKF.weekBeginning, TSKF.weekEnding, \n" +
                "\tTRIM(TRAILING '.' FROM (TRIM(TRAILING '0' FROM (TSKF.DoneActivity - @lastDoneActivity)))) as 'DoneActivity',\n" +
                "      @lastDoneActivity \\:= TSKF.DoneActivity,\n" +
                "\tTSKF.InProgressActivity,\n" +
                "\tTSKF.ToTestActivity\n" +
                "FROM\n" +
                "(SELECT (@rn \\:= @rn + 1) as 'week', weekBeginning, weekEnding, SUM(DoneActivity) as 'DoneActivity', SUM(InProgressActivity) as 'InProgressActivity', SUM(ToTestActivity) as 'ToTestActivity'\n" +
                "FROM\n" +
                "(SELECT DATE(retrievalDate + INTERVAL (1 - DAYOFWEEK(retrievalDate)) DAY) as 'weekBeginning', DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY) as 'weekEnding', tasksClosed as 'DoneActivity', SUM(tasksInProgressDIFF) as 'InProgressActivity', SUM(tasksReadyForTestDIFF) as 'ToTestActivity'\n" +
                "FROM\n" +
                "(select\n" +
                "      TSK.retrievalDate,\n" +
                "      TSK.project,\n" +
                "      TSK.fullName,\n" +
                "      TSK.tasksClosed as 'tasksClosed', \n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksInProgress - @lasttasksInProgress), TSK.tasksInProgress) as tasksInProgressDIFF,\n" +
                "      @lasttasksInProgress \\:= TSK.tasksInProgress,\n" +
                "      if( @lastfullName = TSK.fullName, ABS(TSK.tasksReadyForTest - @lasttasksReadyForTest), TSK.tasksReadyForTest ) as tasksReadyForTestDIFF,\n" +
                "      @lastfullName \\:= TSK.fullName,\n" +
                "      @lasttasksReadyForTest \\:= TSK.tasksReadyForTest\n" +
                "   from\n" +
                "      tasktotals TSK,\n" +
                "      ( select @lastfullName \\:= 0,\n" +
                "               @lasttasksInProgress \\:= 0, \n" +
                "               @lasttasksReadyForTest \\:= 0) SQLVars\n" +
                "\tWHERE project = ?1\n" +
                "    AND email = ?2\n" +
                "   order by\n" +
                "      TSK.fullName,\n" +
                "      TSK.retrievalDate) query1\n" +
                "      GROUP BY fullName, DATE(retrievalDate + INTERVAL (7 - DAYOFWEEK(retrievalDate)) DAY)) query2,\n" +
                "      (select @rn \\:= 0) vars\n" +
                "      GROUP BY weekBeginning) TSKF, \n" +
                "      ( select @lastDoneActivity \\:= 0) SQLVars\n" +
                "      ", WeeklyUpdateActivity.class);
                query.setParameter(1, project);
                query.setParameter(2, student);
                List<WeeklyUpdateActivity> resultList = query.getResultList();
                return resultList;
    }

}
