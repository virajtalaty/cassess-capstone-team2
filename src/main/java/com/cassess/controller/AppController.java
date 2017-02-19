package com.cassess.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cassess.entity.slack.UserObject;
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
}
