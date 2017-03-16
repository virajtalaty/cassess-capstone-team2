package com.cassess.service.taiga;

import com.cassess.dao.CAssessDAO;
import com.cassess.entity.rest.Course;
import com.cassess.entity.taiga.Project;
import com.cassess.entity.taiga.Slugs;
import com.cassess.service.rest.ICourseService;
import com.cassess.service.rest.IStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.util.List;

@Service
@Transactional
public class ProjectService {

    private RestTemplate restTemplate;
    private String projectURL;


    @Autowired
    private CAssessDAO projectStoreDao;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IStudentsService studentsService;



    public ProjectService() {
        restTemplate = new RestTemplate();
        projectURL = "https://api.taiga.io/api/v1/projects/by_slug?slug=";

    }

    public Project getProjectInfo(String token, String slug) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        //Console Output for testing purposes
        System.out.println("Fetching from " + projectURL);

        ResponseEntity<Project> project = restTemplate.getForEntity(projectURL + slug, Project.class, request);

        return projectStoreDao.save(project.getBody());
    }

    /* Method to provide single operation on
    updating the projects table based on the course and student tables
     */
    public void updateProjects(String course){
        Course tempCourse = (Course) courseService.read(course);
        String token = tempCourse.getTaiga_token();
        List<Slugs> slugList = studentsService.listGetSlugs(course);
        for(Slugs slug:slugList){
            getProjectInfo(token, slug.getSlug());
        }
    }
}
