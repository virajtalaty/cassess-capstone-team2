package com.cassess.dao.taiga;

import com.cassess.entity.taiga.MemberData;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

public interface IMemberQueryDao {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    List<MemberData> getMembers(String roleName) throws DataAccessException;
}
