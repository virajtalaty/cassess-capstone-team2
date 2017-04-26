package edu.asu.cassess.dao.slack;

import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.slack.UserObject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@Transactional
public class UserObjectQueryDao implements IUserObjectQueryDao {

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
        public <T> Object getUserByEmail(String email) throws DataAccessException {
            Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.slack_user WHERE email = ?1", UserObject.class);
            query.setParameter(1, email);
            List<UserObject> resultList = query.getResultList();
            if(!resultList.isEmpty()){
                return (UserObject) resultList.get(0);
            }else{
                return new RestResponse("Slack User does not exist for email :" + email);
            }
        }

    @Override
    public <T> List<UserObject> getUsersByEmail(String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.slack_user WHERE email = ?1", UserObject.class);
        query.setParameter(1, email);
        List<UserObject> resultList = query.getResultList();
        return resultList;
    }
}
