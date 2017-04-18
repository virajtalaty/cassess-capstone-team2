package edu.asu.cassess.dao.github;

import edu.asu.cassess.persist.entity.github.GitHubWeight;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;

@Component
public class GitHubWeightDao {
    protected EntityManager entityManager;

    public EntityManager getEntityManager(){
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional
    public GitHubWeight getWeight(String email, Date date){
        GitHubWeight weight;

        try{
            weight = (GitHubWeight) entityManager
                    .createQuery("SELECT w " +
                                         "FROM GitHubWeight " +
                                         "WHERE w.email = ?1 AND w.date = ?2")
                    .setParameter(1, email)
                    .setParameter(2, date)
                    .getSingleResult();
        }catch(Exception e){
            weight = null;
        }

        return weight;
    }

}
