package edu.asu.cassess.dao.slack;

import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.persist.entity.slack.SlackMessageTotals;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public interface ISlackMessageDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    @Transactional
    long getTimeOfLastMessage(String channel);

    @Transactional
    List<SlackMessageTotals> getStudentMessagesInf(String course, String team, String user, String day);

    @Transactional
    List<DailyMessageTotals> getStudentDailyTotals(String course, String team, String user, long start, long end);

    @Transactional
    List<SlackMessageTotals> getTeamMessagesInf(String course, String team, String day);

    @Transactional
    List<DailyMessageTotals> getTeamDailyTotals(String course, String team, long start, long end);

    @Transactional
    List<SlackMessageTotals> getCourseMessagesInf(String course, String day);

    @Transactional
    List<DailyMessageTotals> getCourseDailyTotals(String course, long start, long end);

}
