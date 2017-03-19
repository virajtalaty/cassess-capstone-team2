package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.cassess.persist.entity.taiga.MemberData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@Transactional
public class MemberQueryDao implements IMemberQueryDao{

    protected EntityManager entityManager;

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
    public List<MemberData> getMembers(String roleName, String project_slug) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.memberdata WHERE roleName != ?1 AND project_slug = ?2", MemberData.class);
        query.setParameter(1, roleName);
        query.setParameter(2, project_slug);
        List<MemberData> resultList = query.getResultList();
        return resultList;
    }

}