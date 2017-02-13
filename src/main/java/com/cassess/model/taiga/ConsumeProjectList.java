package com.cassess.model.taiga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
@Transactional
public class ConsumeProjectList {

    private RestTemplate restTemplate;
    private String projectListURL;
    private ConsumeAuthUser testUser;


    @Autowired
    private ProjectStoreDaoImpl projectStoreDao;

    @Autowired
    private ProjectQueryDaoImpl projectQueryDao;

    public ConsumeProjectList() {
        restTemplate = new RestTemplate();
        projectListURL = "https://api.taiga.io/api/v1/projects";

    }

    public Project getProjectInfo(String token, Long id) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        projectListURL = projectListURL + "?member=" + id;

        //Console Output for testing purposes
        System.out.println("Fetching from " + projectListURL);

        ResponseEntity<Project[]> projectList = restTemplate.getForEntity(projectListURL, Project[].class, request);

        Project[] projects = projectList.getBody();

        projects[0].setRetrievalDate();
        return projectStoreDao.save(projects[0]);
    }

    public String getName(){
        Project testProject;
        testProject = projectQueryDao.getProject();
        return testProject.getName();
    }
}
