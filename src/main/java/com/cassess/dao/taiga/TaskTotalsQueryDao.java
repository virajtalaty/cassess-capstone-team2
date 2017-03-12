package com.cassess.dao.taiga;

import com.cassess.entity.taiga.TaskTotals;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class TaskTotalsQueryDao implements ITaskTotalsQueryDao {

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
    @Transactional
    public List<TaskTotals> getTaskTotals() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM cassess.tasktotals", TaskTotals.class);
        List<TaskTotals> resultList = query.getResultList();
        return resultList;
    }

}
