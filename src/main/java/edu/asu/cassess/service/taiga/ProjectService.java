package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dao.CAssessDAO;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.taiga.Project;
import edu.asu.cassess.persist.entity.taiga.Slugs;
import edu.asu.cassess.persist.repo.taiga.ProjectRepo;
import edu.asu.cassess.service.GenericServiceImpl;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.IStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.util.List;

import static javafx.scene.input.KeyCode.T;

@Service
@Transactional
public class ProjectService {

    private RestTemplate restTemplate;
    private String projectURL;


    @Autowired
    private ProjectRepo projectStoreDao;

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
        System.out.println("Updating Projects");
        if (courseService.read(course).getClass() == Course.class){
            Course tempCourse = (Course) courseService.read(course);
            String token = tempCourse.getTaiga_token();
            List<Slugs> slugList = studentsService.listGetSlugs(course);
            for(Slugs slug:slugList){
                System.out.println("Slug: " + slug);
                getProjectInfo(token, slug.getSlug());
            }
        }

    }
}