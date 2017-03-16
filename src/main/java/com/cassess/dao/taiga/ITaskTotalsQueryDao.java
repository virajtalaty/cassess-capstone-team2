package com.cassess.dao.taiga;

import com.cassess.entity.taiga.TaskTotals;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface ITaskTotalsQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<TaskTotals> getTaskTotals() throws DataAccessException;
}
