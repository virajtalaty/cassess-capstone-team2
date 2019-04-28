package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.slack.SlackMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Component
public class SlackMessageDao implements ISlackMessageDao {

    protected EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {return entityManager;}

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<SlackMessage> getMessages(String user, Date start, Date end) {
        List<SlackMessage> messages;
        try {
            messages = (List<SlackMessage>) entityManager
                    .createQuery("SELECT m FROM SlackMessage m WHERE m.user = ?1 and m.ts<=?2 and m.ts>?3")
                    .setParameter(1, user)
                    .setParameter(2, start.getTime())
                    .setParameter(3, end.getTime())
                    .getResultList();
        }catch (Exception e){
            messages = null;
        }
        return messages;
    }

    @Override
    @Transactional
    public Date getTimeOfLastMessage() {

        return (Date)entityManager
                .createQuery("SELECT MAX(m.date) FROM SlackMessage m")
                .getSingleResult();
    }

    @Override
    @Transactional
    public int getMessageCount(String user, Date start, Date end) {
        return getMessages(user,start,end).size();
    }
}
