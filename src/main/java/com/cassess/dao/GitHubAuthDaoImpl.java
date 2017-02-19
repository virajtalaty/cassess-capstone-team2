package com.cassess.dao;
import com.cassess.model.github.GitHubAuth;
import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class GitHubAuthDaoImpl extends GenericDAOImpl<GitHubAuth, Long> {

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
    public GitHubAuth save(GitHubAuth gitHubAuth) {
        if(em.find(GitHubAuth.class, gitHubAuth.getId()) != null){
            em.merge(gitHubAuth);
        }else{
            em.persist(gitHubAuth);
        }
        return gitHubAuth;
    }
}