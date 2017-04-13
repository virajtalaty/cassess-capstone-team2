package edu.asu.cassess.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.cassess.service.taiga.TaskDataService;

@RestController
public class TaskController {

	@Autowired
	TaskDataService taigaDataService;
	
	@Scheduled(cron="0 20 * * * ?")
	public void TaigaTasks() {
		System.out.println("timer running");
		taigaDataService.updateTaskTotals("SER_402");
	}
}
