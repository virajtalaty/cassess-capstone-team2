package edu.asu.cassess.dao.taiga;

import org.springframework.dao.DataAccessException;

import edu.asu.cassess.persist.entity.taiga.MemberData;

import javax.persistence.EntityManager;
import java.util.List;

public interface IMemberQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<MemberData> getMembers(String roleName, String project_slug) throws DataAccessException;
}
