package com.cassess.model.taiga;

import com.cassess.entity.CommitData;
import com.cassess.model.github.GitHubCommitData;
import com.cassess.model.github.GitHubSha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Thomas on 2/9/2017.
 */

@Service
@Transactional
public class GetTaskData {

    private RestTemplate restTemplate;

    private String membershipListURL;
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


    }

    public TaskData[] getTasks(Long projectId, String token, int page) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        headers.add("x-disable-pagination", "True");

        System.out.println("Headers: " + headers);

        HttpEntity<String> request = new HttpEntity<>(headers);

        //Console Output for testing purposes
        //System.out.println("Request " + request);

        tasksListURL = tasksListURL + projectId + "&page=" + page;

        //Console Output for testing purposes
        //System.out.println("Fetching from " + projectListURL);

        ResponseEntity<TaskData[]> taskList = restTemplate.getForEntity(tasksListURL, TaskData[].class, request);

        TaskData[] tasks = taskList.getBody();

        System.out.println("Number Results: " + tasks.length);

        for (int i = 0; i < tasks.length - 1; i++) {

            //System.out.println(tasks[i].getProject());
            //System.out.println("Full Name:  " + tasks[i].getAssignmentData().getFull_name_display());
            //System.out.println("Member ID:  " + tasks[i].getAssignmentData().getId());
            //System.out.println("Username:  " + tasks[i].getAssignmentData().getUsername());
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

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        System.out.println("Headers: " + headers);

        HttpEntity<String> request = new HttpEntity<>(headers);

        //Console Output for testing purposes
        //System.out.println("Request " + request);

        membershipListURL = membershipListURL + projectId + "&page=" + page;

        //Console Output for testing purposes
        //System.out.println("Fetching from " + projectListURL);

        ResponseEntity<MemberData[]> memberList = restTemplate.getForEntity(membershipListURL, MemberData[].class, request);

        MemberData[] members = memberList.getBody();

        System.out.println("Number Results: " + members.length);

        for (int i = 0; i < members.length - 1; i++) {

            //System.out.println(tasks[i].getProject());
            //System.out.println("Full Name:  " + tasks[i].getAssignmentData().getFull_name_display());
            //System.out.println("Member ID:  " + tasks[i].getAssignmentData().getId());
            //System.out.println("Username:  " + tasks[i].getAssignmentData().getUsername());
            MemberDao.save(members[i]);
        }
        System.out.println("Headers Response" + memberList.getHeaders());

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
            int openTasks = newTasks+inProgressTasks+readyForTestTasks;
            TaskTotalsDao.save(new TaskTotals(member.getId(), name, member.getProject_name(), member.getRole_name(), closedTasks, newTasks, inProgressTasks, inProgressTasks, openTasks));
        }
    }


}

