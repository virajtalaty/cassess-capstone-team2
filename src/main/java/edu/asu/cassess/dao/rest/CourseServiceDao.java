package edu.asu.cassess.dao.rest;

import edu.asu.cassess.persist.entity.rest.*;
import edu.asu.cassess.model.Taiga.CourseList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.service.rest.AdminsService;
import edu.asu.cassess.service.rest.StudentsService;
import edu.asu.cassess.service.rest.TeamsService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import edu.asu.cassess.persist.repo.rest.CourseRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Component
public class CourseServiceDao {

    @Autowired
    private CourseRepo courseRepo;



    @Autowired
    private TeamsService teamsService;

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private StudentsService studentsService;

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public <T> Object create(Course courseInput) {
        Course course = (Course) courseRepo.findOne(courseInput.getCourse());
        List<Team> teams = courseInput.getTeams();
        List<Admin> admins = courseInput.getAdmins();
        if (course != null) {
            return new RestResponse(course.getCourse() + " already exists in database");
        } else {
            courseRepo.save(courseInput);
            teamsService.listCreate(teams);
            adminsService.listCreate(admins);
            for(Team team:teams){
                for(Student student:team.getStudents())
                studentsService.studentUser(student);
            }
            for(Admin admin:admins){
                adminsService.adminUser(admin);
            }
            return courseInput;
        }
    }

    @Transactional
    public <T> Object update(Course courseInput) {
        Course course = (Course) courseRepo.findOne(courseInput.getCourse());
        List<Team> teams = courseInput.getTeams();
        List<Admin> admins = courseInput.getAdmins();
        if (course != null) {
            courseRepo.save(courseInput);
            teamsService.listUpdate(teams);
            adminsService.listUpdate(admins);
            return courseInput;
        } else {
            return new RestResponse(courseInput + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String courseName) {
        Course course = (Course) courseRepo.findOne(courseName);
        if (course != null) {
            return course;
        } else {
            return new RestResponse(courseName + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String courseName) {
        Course course = (Course) courseRepo.findOne(courseName);
        if (course != null) {
            courseRepo.delete(course);
            return new RestResponse(course + " has been removed from the database");
        } else {
            return new RestResponse(course + " does not exist in the database");
        }
    }

    @Transactional
    public List<Course> listRead() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.courses", Course.class);
        List<Course> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<CourseList> listGetCourses() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT course FROM cassess.courses", CourseList.class);
        List<CourseList> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public JSONObject listCreate(List<Course> courses) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Course course : courses) {
            if (courseRepo.findOne(course.getCourse()) != null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " already exists in the database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                courseRepo.save(course);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(course)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("success", successArray);
        returnJSON.put("failure", failureArray);
        return returnJSON;
    }

    @Transactional
    public JSONObject listUpdate(List<Course> courses) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Course course : courses) {
            if (courseRepo.findOne(course.getCourse()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                courseRepo.save(course);
                 try {
                     successArray.put(new JSONObject(ow.writeValueAsString(course)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("success", successArray);
        returnJSON.put("failure", failureArray);
        return returnJSON;
    }
}