package edu.asu.cassess.dao.taiga;

import edu.asu.cassess.model.Taiga.*;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.TaskTotals;

import javax.persistence.EntityManager;
import java.util.List;

public interface ITaskTotalsQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);


    List<TaskTotals> getTaskTotals() throws DataAccessException;

    RestResponse deleteTaskTotalsByCourse(Course course) throws DataAccessException;

    RestResponse deleteTaskTotalsByProject(Team team) throws DataAccessException;

    RestResponse deleteTaskTotalsByStudent(Student student) throws DataAccessException;

    List<DailyTaskTotals> getDailyTasksByProject(String beginDate, String endDate, String course, String project);

    List<DailyTaskTotals> getDailyTasksByStudent(String beginDate, String endDate, String course, String project, String student);

    List<WeeklyIntervals> getWeeklyIntervalsByStudent(String course, String project, String student);

    List<WeeklyIntervals> getWeeklyIntervalsByProject(String course, String project);

    List<WeeklyUpdateActivity> getWeeklyUpdatesByProject(String course, String project);

    List<WeeklyUpdateActivity> getWeeklyUpdatesByStudent(String course, String project, String student);

    List<WeeklyUpdateActivity> getWeeklyUpdatesByCourse(String course);

    List<WeeklyWeight> lastTwoWeekWeightsByStudent(String course, String project, String student);

    List<WeeklyWeight> lastTwoWeekWeightsByProject(String course, String project);

    List<WeeklyWeight> lastTwoWeekWeightsByCourse(String course);

    List<WeeklyWeight> getWeeklyWeightByStudent(String course, String project, String student);

    List<WeeklyWeight> getWeeklyWeightByProject(String course, String project);

    List<WeeklyWeight> getWeeklyWeightByCourse(String course);

    List<WeeklyAverages> getWeeklyAverageByCourse(String course);

    List<WeeklyAverages> getWeeklyAverageByProject(String course, String project);

    List<WeeklyAverages> getWeeklyAverageByStudent(String course, String project, String email);

    List<WeeklyAverages> lastTwoWeekAveragesByCourse(String course);

    List<WeeklyAverages> lastTwoWeekAveragesByProject(String course, String project);

    List<WeeklyAverages> lastTwoWeekAveragesByStudent(String course, String project, String email);
}
