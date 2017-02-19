package com.cassess.dao.slack;

import java.util.List;

import com.cassess.entity.slack.UserObject;

public interface ISlackDAO {

	UserObject getUserById(String uid);

	List<UserObject> getAllUsers();
	
	String getSlackToken();
	
	void saveUserList(List<UserObject> userList);
	
}
