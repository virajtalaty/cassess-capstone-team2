package com.cassess.model.taiga;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class TaskQueryDaoImpl{

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public int getClosedTasks(String full_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT COUNT(*) AS total FROM cassess.taskdata WHERE status = 'Closed' AND full_name = ?1", TaskCount.class);
        query.setParameter(1, full_name);
        List<TaskCount> taskCount =  query.getResultList();
        int result = taskCount.get(0).getTotal();
        getEntityManager().createNativeQuery("DROP TABLE IF EXISTS taskcount;").executeUpdate();
        return result;
    }

    @Transactional
    public int getNewTasks(String full_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT COUNT(*) AS total FROM cassess.taskdata WHERE status = 'New' AND full_name = ?1", TaskCount.class);
        query.setParameter(1, full_name);
        List<TaskCount> taskCount =  query.getResultList();
        int result = taskCount.get(0).getTotal();
        getEntityManager().createNativeQuery("DROP TABLE IF EXISTS taskcount;").executeUpdate();
        return result;
    }

    @Transactional
    public int getInProgressTasks(String full_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT COUNT(*) AS total FROM cassess.taskdata WHERE status = 'In Progress' AND full_name = ?1", TaskCount.class);
        query.setParameter(1, full_name);
        List<TaskCount> taskCount =  query.getResultList();
        int result = taskCount.get(0).getTotal();
        getEntityManager().createNativeQuery("DROP TABLE IF EXISTS taskcount;").executeUpdate();
        return result;
    }

    @Transactional
    public int getReadyForTestTasks(String full_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT COUNT(*) AS total FROM cassess.taskdata WHERE status = 'Ready For Test' AND full_name = ?1", TaskCount.class);
        query.setParameter(1, full_name);
        List<TaskCount> taskCount =  query.getResultList();
        int result = taskCount.get(0).getTotal();
        getEntityManager().createNativeQuery("DROP TABLE IF EXISTS taskcount;").executeUpdate();
        return result;
    }

}