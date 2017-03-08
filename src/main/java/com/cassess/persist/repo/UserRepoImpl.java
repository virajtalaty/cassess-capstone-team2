package com.cassess.persist.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cassess.dao.CAssessDAO;
import com.cassess.persist.entity.User;

@Repository
public class UserRepoImpl implements UserRepo {

	@Autowired
	private CAssessDAO dao;
	
	@Override
	public User findByLogin(String login) {
		return dao.find(User.class, login);
	}

}
