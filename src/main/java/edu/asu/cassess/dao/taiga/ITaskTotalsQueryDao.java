package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.model.Taiga.DailyTaskTotals;
import edu.asu.cassess.model.Taiga.DisplayAllTasks;
import edu.asu.cassess.model.Taiga.WeeklyIntervals;
import edu.asu.cassess.model.Taiga.WeeklyUpdateActivity;
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

    List<DailyTaskTotals> getDailyTasksByProject(String beginDate, String endDate, String project);

    List<DailyTaskTotals> getDailyTasksByStudent(String beginDate, String endDate, String project, String student);

    List<WeeklyIntervals> getWeeklyIntervalsByStudent(String project, String student);

    List<WeeklyIntervals> getWeeklyIntervalsByProject(String project);

    List<WeeklyUpdateActivity> getWeeklyUpdatesByProject(String project);

    List<WeeklyUpdateActivity> getWeeklyUpdatesByStudent(String project, String student);
}
