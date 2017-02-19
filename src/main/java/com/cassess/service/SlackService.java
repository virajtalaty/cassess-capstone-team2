package com.cassess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cassess.dao.slack.ISlackDAO;
import com.cassess.entity.slack.UserList;
import com.cassess.entity.slack.UserObject;

@Service
public class SlackService implements ISlackService {

	@Autowired
	private ISlackDAO slackDAO;
	
	private RestTemplate restTemplate;
	private String baseURL;
	private String token;
	
	@Override
	public List<UserObject> getAllUsers() {
		return slackDAO.getAllUsers();
	}
	
	@Override
	public void fetchSaveUserList() {
		restTemplate = new RestTemplate();
		baseURL = "https://slack.com/api/";
		token = slackDAO.getSlackToken();
		String ulURL = baseURL + "users.list" + token;
		UserList ul = restTemplate.getForObject(ulURL, UserList.class);
		slackDAO.saveUserList(ul.getMembers());
	}
}
