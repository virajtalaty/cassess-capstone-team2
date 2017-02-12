package com.cassess.model.slack;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;

@Component
public class UserDaoImpl extends GenericDAOImpl<UserObject, Long> {
	
    @Override
    @Autowired(required=true)
    public void setSearchProcessor(JPASearchProcessor searchProcessor) {
           super.setSearchProcessor(searchProcessor);
    }

    @Override
    @PersistenceContext(unitName="default")
    public void setEntityManager(EntityManager entityManager) {
           super.setEntityManager(entityManager);
    }
}
