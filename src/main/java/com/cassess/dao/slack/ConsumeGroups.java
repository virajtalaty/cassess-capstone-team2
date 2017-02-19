package com.cassess.dao.slack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cassess.dao.CAssessDAO;
import com.cassess.entity.slack.GroupList;
import com.cassess.entity.slack.SlackAuth;

@Service
@Transactional
public class ConsumeGroups {
	
	private RestTemplate restTemplate;
	private String baseURL;
	private String token;
	private SlackAuth auth;
	
	@Autowired
	private CAssessDAO dao;
	
	public ConsumeGroups() {
		restTemplate = new RestTemplate();
		baseURL = "https://slack.com/api/";
		auth = new SlackAuth();
	}
	
	public GroupList getGroupList() {
		auth = dao.find(SlackAuth.class, 1);
		token = auth.getToken();
		String glURL = baseURL + "groups.list" + token;
		System.out.println("Fetching from " + glURL);
		GroupList retGroups = restTemplate.getForObject(glURL, GroupList.class);
		return retGroups;
	}
}
