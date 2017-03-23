package edu.asu.cassess.dao.slack;

import java.util.List;

import edu.asu.cassess.persist.entity.slack.UserObject;

public interface ISlackDAO {

	UserObject getUserById(String uid);

	List<UserObject> getAllUsers();
	
	String getSlackToken();
	
	void saveUserList(List<UserObject> userList);
	
}
