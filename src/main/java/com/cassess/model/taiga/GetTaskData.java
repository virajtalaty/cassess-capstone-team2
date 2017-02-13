package com.cassess.model.taiga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Created by Thomas on 2/9/2017.
 */

@Service
@Transactional
public class GetTaskData {

    private RestTemplate restTemplate;

    private String membershipListURL;
    private String membershipDetailURL;
    private String tasksListURL;

    @Autowired
    private TasksDaoImpl TaskDao;

    @Autowired
    private MembersDaoImpl MemberDao;

    @Autowired
    private TaskQueryDaoImpl TaskQueryDao;

    @Autowired
    private MemberQueryDaoImpl MemberQueryDao;

    @Autowired
    private TaskTotalsDaoImpl TaskTotalsDao;



    public GetTaskData() {
        restTemplate = new RestTemplate();
        membershipListURL = "https://api.taiga.io/api/v1/memberships?project=";
        tasksListURL = "https://api.taiga.io/api/v1/tasks?project=";
        membershipDetailURL = "https://api.taiga.io/api/v1/memberships/";


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

    public MemberData[] getMembers(Long projectId, String token, int page) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("Authorization", "Bearer " + token);

        System.out.println("Headers: " + headers);

        HttpEntity<String> request = new HttpEntity<>(headers);

        membershipListURL = membershipListURL + projectId + "&page=" + page;

        ResponseEntity<MemberData[]> memberList = restTemplate.getForEntity(membershipListURL, MemberData[].class, request);

        MemberData[] members = memberList.getBody();

        for (int i = 0; i < members.length; i++) {
            MemberDao.save(members[i]);
        }

        if (memberList.getHeaders().containsKey("x-pagination-next")) {
            page++;
            return getMembers(projectId, token, page);
        } else {

            return members;
        }
    }

    public void getTaskTotals() {
        List<MemberData> memberNames = MemberQueryDao.getMembersByRole("Back");
        for (MemberData member: memberNames) {
            String name = member.getFull_name();
            int closedTasks = TaskQueryDao.getClosedTasks(name);
            int newTasks = TaskQueryDao.getNewTasks(name);
            int inProgressTasks = TaskQueryDao.getInProgressTasks(name);
            int readyForTestTasks = TaskQueryDao.getReadyForTestTasks(name);
            int openTasks = newTasks + inProgressTasks + readyForTestTasks;
            TaskTotalsDao.save(new TaskTotals(member.getId(), name, member.getProject_name(), member.getRole_name(), closedTasks, newTasks, inProgressTasks,
                    readyForTestTasks, openTasks));
        }
    }


}

