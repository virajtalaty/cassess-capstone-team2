package com.cassess.model.slack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cassess.model.CAssessDAO;

@Service
@Transactional
public class ConsumeChannels {
	
	private RestTemplate restTemplate;
	private String baseURL;
	private String token;
	private SlackAuth auth;
	
	@Autowired
	private CAssessDAO dao;
	
	public ConsumeChannels() {
		restTemplate = new RestTemplate();
		baseURL = "https://slack.com/api/";
		auth = new SlackAuth();
	}
	
	public ChannelsList getChannelsList() {
		auth = dao.find(SlackAuth.class, 1);
		token = auth.getToken();
		String clURL = baseURL + "channels.list" + token;
		System.out.println("Fetching from " + clURL);
		ChannelsList retList = restTemplate.getForObject(clURL, ChannelsList.class);
		System.out.println("Some info from data storage class " + retList.getChannels().size());
		for (ChannelObject obj : retList.getChannels()) {
			dao.save(obj);
		}
		return retList;
	}
}
