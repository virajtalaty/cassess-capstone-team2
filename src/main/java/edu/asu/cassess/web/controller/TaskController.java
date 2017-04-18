package edu.asu.cassess.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.cassess.service.taiga.ITaskDataService;

@RestController
@PropertySource("classpath:scheduling.properties")
public class TaskController {

	@Autowired
	ITaskDataService taigaDataService;
	
	@Scheduled(cron = "${taiga.cron.expression}")
	public void TaigaTasks() {
		System.out.println("cron ran as scheduled");
		//taigaDataService = new TaskDataService();
		taigaDataService.updateTaskTotals("SER_402");
	}
}
