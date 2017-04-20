package edu.asu.cassess.dao.GitHub;

import edu.asu.cassess.model.Taiga.WeeklyFreqWeight;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Thomas on 4/19/2017.
 */
public interface IGitHubQueryDao {
    EntityManager getEntityManager();

    @PersistenceContext
    void setEntityManager(EntityManager entityManager);

    @Transactional
    List<WeeklyFreqWeight> getWeightFreqByCourse(String course) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> getWeightFreqByTeam(String course, String team) throws DataAccessException;

    @Transactional
    List<WeeklyFreqWeight> getWeightFreqByStudent(String course, String team, String email) throws DataAccessException;
}
