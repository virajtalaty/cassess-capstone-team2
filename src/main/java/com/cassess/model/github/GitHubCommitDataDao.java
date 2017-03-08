package com.cassess.model.github;

import com.cassess.entity.github.CommitData;
import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.List;

@Component
public class GitHubCommitDataDao extends GenericDAOImpl<CommitData, Long>{
    protected EntityManager entityManager;


    @Override
    @Autowired(required=true)
    public void setSearchProcessor(JPASearchProcessor searchProcessor) {
        super.setSearchProcessor(searchProcessor);
    }

    @Override
    @PersistenceContext(unitName="default")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
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
