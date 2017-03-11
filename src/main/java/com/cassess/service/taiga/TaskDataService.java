package com.cassess.service.taiga;
import com.cassess.entity.taiga.MemberData;
import com.cassess.entity.taiga.TaskData;
import com.cassess.entity.taiga.TaskTotals;
import com.cassess.entity.taiga.TaskTotalsID;
import com.cassess.dao.taiga.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class TaskDataService {

    private RestTemplate restTemplate;

    private String tasksListURL;

    @Autowired
    private TasksDaoImpl TaskDao;


    @Autowired
    private TaskQueryDaoImpl TaskQueryDao;

    @Autowired
    private MemberQueryDaoImpl MemberQueryDao;

    @Autowired
    private TaskTotalsDaoImpl TaskTotalsDao;



    public TaskDataService() {
        restTemplate = new RestTemplate();
        tasksListURL = "https://api.taiga.io/api/v1/tasks?project=";
    }

    public TaskData[] getTasks(Long projectId, String token, int page) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        headers.add("x-disable-pagination", "True");

        System.out.println("Headers: " + headers);

        HttpEntity<String> request = new HttpEntity<>(headers);

        tasksListURL = tasksListURL + projectId + "&page=" + page;

        ResponseEntity<TaskData[]> taskList = restTemplate.getForEntity(tasksListURL, TaskData[].class, request);

        TaskData[] tasks = taskList.getBody();

        System.out.println("Number Results: " + tasks.length);

        for (int i = 0; i < tasks.length - 1; i++) {

            TaskDao.save(tasks[i]);
        }
        System.out.println("Headers Response" + taskList.getHeaders());

        if (taskList.getHeaders().containsKey("x-pagination-next")) {
            page++;
            return getTasks(projectId, token, page);
        } else {

            return tasks;
        }
    }

    public void getTaskTotals() {
        List<MemberData> memberNames = MemberQueryDao.getMembers("Product Owner");
        for (MemberData member: memberNames) {
            String name = member.getFull_name();
            int closedTasks = TaskQueryDao.getClosedTasks(name);
            int newTasks = TaskQueryDao.getNewTasks(name);
            int inProgressTasks = TaskQueryDao.getInProgressTasks(name);
            int readyForTestTasks = TaskQueryDao.getReadyForTestTasks(name);
            int openTasks = newTasks + inProgressTasks + readyForTestTasks;

            TaskTotalsDao.save(new TaskTotals(new TaskTotalsID(member.getId()), name, member.getProject_name(), member.getRole_name(), closedTasks, newTasks, inProgressTasks,
                    readyForTestTasks, openTasks));
        }
    }



}

