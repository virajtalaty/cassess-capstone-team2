package com.cassess.service.DAO;
import com.cassess.entity.AuthToken;
import com.cassess.model.RestResponse;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class AuthTokenServiceDaoImpl extends AuthTokenServiceDao {

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
    public <T> Object create(AuthToken authToken) {
        if(em.find(AuthToken.class, authToken.getTool()) != null){
            return new RestResponse("tool already exists in database");
        }else{
            em.persist(authToken);
            return authToken;
        }
    }

    @Transactional
    public <T> Object update(AuthToken authToken) {
        if(em.find(AuthToken.class, authToken.getTool()) != null){
            em.merge(authToken);
            return authToken;
        }else{
            return new RestResponse("tool does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String tool) {
        AuthToken authToken = em.find(AuthToken.class, tool);
        if(authToken != null){
            return authToken;
        }else{
            return new RestResponse("tool does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String tool) {
        AuthToken authToken = em.find(AuthToken.class, tool);
        if(authToken != null){
            em.remove(authToken);
            return new RestResponse(tool + " has been removed from the database");
        }else{
            return new RestResponse(tool + " does not exist in the database");
        }
    }
}