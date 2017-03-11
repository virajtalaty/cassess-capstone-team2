package com.cassess.dao.taiga;

import com.cassess.entity.taiga.MemberData;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class MemberQueryDaoImpl{

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List<MemberData> getMembers(String roleName) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM memberdata WHERE roleName != ?1", MemberData.class);
        query.setParameter(1, roleName);
        List<MemberData> resultList = query.getResultList();
        return resultList;
    }

}