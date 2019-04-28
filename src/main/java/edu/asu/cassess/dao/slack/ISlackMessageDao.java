package edu.asu.cassess.dao.slack;

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
    List<SlackMessage> getMessages(String user, Date start, Date end);

    @Transactional
    Date getTimeOfLastMessage();

    @Transactional
    int getMessageCount(String user, Date start, Date end);

}
