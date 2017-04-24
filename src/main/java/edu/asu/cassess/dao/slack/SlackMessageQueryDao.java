package edu.asu.cassess.dao.slack;

import edu.asu.cassess.model.slack.MessageCount;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.repo.slack.SlackMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class SlackMessageQueryDao implements ISlackMessageQueryDao{

    protected EntityManager entityManager;

    @Autowired
    private SlackMessageRepo slackMessageRepo;

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
    @Transactional
    public RestResponse truncateMessageData() {
        slackMessageRepo.deleteAll();
        return new RestResponse("slackmessage table date has been deleted");
    }

    @Override
    @Transactional
    public int getMessageCount(String user) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT COUNT(*) AS total FROM cassess.slack_messages WHERE user = ?1 AND LENGTH(text) > 20", MessageCount.class);
        query.setParameter(1, user);
        List<MessageCount> messageCount = query.getResultList();
        int result = messageCount.get(0).getTotal();
        return result;
    }
}
