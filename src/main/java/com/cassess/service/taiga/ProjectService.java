package com.cassess.service.taiga;

import com.cassess.entity.taiga.Project;
import com.cassess.dao.taiga.ProjectQueryDaoImpl;
import com.cassess.dao.taiga.ProjectStoreDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
@Transactional
public class ProjectService {

    private RestTemplate restTemplate;
    private String projectURL;


    @Autowired
    private ProjectStoreDaoImpl projectStoreDao;

    public ProjectService() {
        restTemplate = new RestTemplate();
        projectURL = "https://api.taiga.io/api/v1/projects/by_slug";

    }

    public Project getProjectInfo(String token, String slug) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        projectURL = projectURL + "?slug=" + slug;

        //Console Output for testing purposes
        System.out.println("Fetching from " + projectURL);

        ResponseEntity<Project> project = restTemplate.getForEntity(projectURL, Project.class, request);

        return projectStoreDao.save(project.getBody());
    }
}
