package com.cassess.service.DAO;
import com.cassess.model.github.GitHubRest;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class GitHubRestServiceDaoImpl extends GitHubRestServiceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Autowired(required = true)
    public void setSearchProcessor(JPASearchProcessor searchProcessor) {
        super.setSearchProcessor(searchProcessor);
    }

    @Override
    @PersistenceContext(unitName = "default")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

    @Transactional
    public GitHubRest save(GitHubRest gitHubRest) {
        if(em.find(GitHubRest.class, gitHubRest.getId()) != null){
            em.merge(gitHubRest);
        }else{
            em.persist(gitHubRest);
        }
        return gitHubRest;
    }
}