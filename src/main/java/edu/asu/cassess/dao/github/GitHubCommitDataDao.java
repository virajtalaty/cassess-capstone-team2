package edu.asu.cassess.dao.github;

import edu.asu.cassess.persist.entity.github.CommitData;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.List;

@Component
public class GitHubCommitDataDao{
    protected EntityManager entityManager;


    public EntityManager getEntityManager() {
        return entityManager;
    }


    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public CommitData getCommit(String username, Date date) throws DataAccessException{

        CommitData commitData;
        try{
            commitData = (CommitData) entityManager
                    .createQuery("SELECT c FROM CommitData c  WHERE c.username = ?1 AND c.date = ?2")
                    .setParameter(1, username)
                    .setParameter(2, date)
                    .getSingleResult();
        }catch(Exception e){
            commitData = null;
        }

        return commitData;
    }

    @Transactional
    public List<CommitData> getAllCommitData() throws DataAccessException{
        return entityManager
                    .createQuery("SELECT c FROM CommitData c")
                    .getResultList();
    }

}
