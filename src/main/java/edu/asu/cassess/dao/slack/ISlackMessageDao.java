package edu.asu.cassess.dao.slack;

import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.persist.entity.slack.SlackMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

public interface ISlackMessageDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    @Transactional
    boolean getMessageExists(double ts);

    @Transactional
    double getTimeOfLastMessage(String channel);

    @Transactional
    List<SlackMessage> getStudentMessages(String user, long start, long end);

    @Transactional
    int getStudentMessageCount(String user, long start, long end);

    @Transactional
    List<DailyMessageTotals> getStudentDailyTotals(String user, long start, long end);

    @Transactional
    List<SlackMessage> getTeamMessages(String team, long start, long end);

    @Transactional
    int getTeamMessageCount(String team, long start, long end);

    @Transactional
    List<DailyMessageTotals> getTeamDailyTotals(String team, long start, long end);

}
