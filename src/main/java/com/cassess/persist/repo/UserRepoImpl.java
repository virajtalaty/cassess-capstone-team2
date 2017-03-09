package com.cassess.persist.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cassess.dao.CAssessDAO;
import com.cassess.persist.entity.User;

import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {

	@Autowired
	private CAssessDAO dao;

	@Override
	public User findByLogin(String login) {
		return dao.find(User.class, login);
	}

	@Override
	public List<User> findAll() {
		return dao.findAll(User.class);
	}

	@Override
	public User findOne(Long userId) {
		return dao.find(User.class, userId);
	}

	@Override
	public User save(User user) {
		return dao.save(user);
	}


}
