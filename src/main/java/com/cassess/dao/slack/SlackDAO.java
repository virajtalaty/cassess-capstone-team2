package com.cassess.dao.slack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cassess.dao.CAssessDAO;
import com.cassess.entity.slack.SlackAuth;
import com.cassess.entity.slack.UserObject;

import java.util.List;

@Transactional
@Repository
public class SlackDAO implements ISlackDAO {
	
	@Autowired
	private CAssessDAO dao;
	
	@Override
	public UserObject getUserById(String uid){
		return dao.find(UserObject.class, uid);
	}
	
	@Override
	public List<UserObject> getAllUsers() {
		return dao.findAll(UserObject.class);
	}

	@Override
	public String getSlackToken() {
		String auth = dao.find(SlackAuth.class, 1).getToken();
		return auth;
	}
	
	@Override
	public void saveUserList(List<UserObject> userList) {
		//for (UserObject obj : userList)
		//	dao.save(obj);
		dao.save(userList.toArray());
	}
}
