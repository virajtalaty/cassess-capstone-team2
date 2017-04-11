package com.cassess.controller;

import java.util.List;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cassess.entity.slack.UserObject;
import com.cassess.jobs.CollectionJob;
import com.cassess.service.ISlackService;

@Controller
public class AppController {

	@Autowired
	ISlackService slackService;
	
	@RequestMapping(value = "/slack_resource", method = RequestMethod.GET)
	public ResponseEntity<List<UserObject>> getAllUsers() {
		slackService.fetchSaveUserList();
		List<UserObject> usrList = (List<UserObject>) slackService.getAllUsers();
		return new ResponseEntity<List<UserObject>>(usrList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/timer_test", method = RequestMethod.GET)
	public ResponseEntity<String> timerTest(@RequestParam("code") String formValue) throws SchedulerException {
		//if post message is equal to 1234, run a timer
		//build the timer based on CollectionJob.class
		//scheduler; trigger; job details
		//timer.html in partials with a field and a button on a POST form
		//Angular controller calls /timer_test
		// Define the job and tie it to our HelloJob class
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		Scheduler sched = schedFact.getScheduler();

		sched.start();
		
		JobDetail job = newJob(CollectionJob.class)
		    .withIdentity("myJob", "group1") // name "myJob", group "group1"
		    .build();
		
		// Trigger the job to run now, and then every 40 seconds
		Trigger trigger = newTrigger()
		    .withIdentity("myTrigger", "group1")
		    .startNow()
		    .withSchedule(simpleSchedule()
		        .withIntervalInSeconds(60)
		        .repeatForever())            
		    .build();
		
		// Tell quartz to schedule the job using our trigger
		if (formValue == "1234")
			sched.scheduleJob(job, trigger);
		return new ResponseEntity<String>("timer", HttpStatus.OK);
	}
}
