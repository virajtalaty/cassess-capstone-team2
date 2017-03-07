package com.cassess.dao.rest;

import com.cassess.entity.rest.Course;
import com.cassess.entity.rest.RestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Component
public class CourseServiceDaoImpl extends CourseServiceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Autowired(required = true)
    public void setSearchProcessor(JPASearchProcessor searchProcessor) {
        super.setSearchProcessor(searchProcessor);
    }

    @Override
    @PersistenceContext(unitName = "default")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }


    @Transactional
    public <T> Object create(Course course) {
        if (em.find(Course.class, course.getCourse()) != null) {
            return new RestResponse(course.getCourse() + " already exists in database");
        } else {
            em.persist(course);
            return course;
        }
    }

    @Transactional
    public <T> Object update(Course course) {
        if (em.find(Course.class, course.getCourse()) != null) {
            em.merge(course);
            return course;
        } else {
            return new RestResponse(course + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String courseName) {
        Course course = em.find(Course.class, courseName);
        if (course != null) {
            return course;
        } else {
            return new RestResponse(courseName + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String course) {
        Course authToken = em.find(Course.class, course);
        if (authToken != null) {
            em.remove(authToken);
            return new RestResponse(course + " has been removed from the database");
        } else {
            return new RestResponse(course + " does not exist in the database");
        }
    }

    @Transactional
    public List<Course> listRead() throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM cassess.courses", Course.class);
        List<Course> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public JSONObject listCreate(List<Course> courses) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Course course : courses) {
            if (em.find(Course.class, course.getCourse()) != null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " already exists in the database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                em.persist(course);
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
            if (em.find(Course.class, course.getCourse()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                em.merge(course);
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