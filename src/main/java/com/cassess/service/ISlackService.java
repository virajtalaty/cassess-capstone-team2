package com.cassess.service;

import java.util.List;

import com.cassess.entity.slack.UserObject;

public interface ISlackService {

	List<UserObject> getAllUsers();
	
	void fetchSaveUserList();

}
