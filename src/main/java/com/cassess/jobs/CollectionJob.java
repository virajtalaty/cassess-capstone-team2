package com.cassess.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cassess.service.ISlackService;

public class CollectionJob implements Job {

	@Autowired
	ISlackService slackService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//slackService.fetchSaveUserList();
		System.out.println("Slack collection job running");
	}

}
