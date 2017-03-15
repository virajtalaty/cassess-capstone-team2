package com.cassess.persist.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cassess.dao.CAssessDAO;
import com.cassess.persist.entity.User;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class UserRepoImpl implements UserRepo {

	@Autowired
	private CAssessDAO dao;
	
    private EntityManager entityManager;

    private EntityManager getEntityManager() {
        return entityManager;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	@Override
	public User findByLogin(String login) {
        User user = (User)getEntityManager().createQuery("SELECT u FROM User u WHERE u.login=:log")
        .setParameter("log", login).getResultList().get(0);
        return user;
	}

	@Override
	public List<User> findAll() {
		return dao.findAll(User.class);
	}

	@Override
	public User findOne(Long id) {
		return dao.find(User.class, id);
	}

	@Override
	public User save(User user) {
		return dao.save(user);
	}


}
