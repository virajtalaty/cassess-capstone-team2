package com.cassess.service.DAO;

import com.cassess.entity.Course;
import com.cassess.entity.slack.UserObject;
import com.cassess.model.RestResponse;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

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
        if(em.find(Course.class, course.getCourse()) != null){
            return new RestResponse(course.getCourse() + " already exists in database");
        }else{
            em.persist(course);
            return course;
        }
    }

    @Transactional
    public <T> Object update(Course course) {
        if(em.find(Course.class, course.getCourse()) != null){
            em.merge(course);
            return course;
        }else{
            return new RestResponse(course.getCourse() + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String tool) {
        Course course = em.find(Course.class, tool);
        if(course != null){
            return course;
        }else{
            return new RestResponse(course.getCourse() + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String course) {
        Course authToken = em.find(Course.class, course);
        if(authToken != null){
            em.remove(authToken);
            return new RestResponse(course + " has been removed from the database");
        }else{
            return new RestResponse(course + " does not exist in the database");
        }
    }

    @Transactional
    public List<Course> listRead() throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM courses");
        List<Course> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Object> listCreate(List<Course> courses) {
        List<Course> returnCourses = new ArrayList<Course>();
        List<RestResponse> returnResponses = new ArrayList<RestResponse>();
        for(Course course:courses)
        if(em.find(Course.class, course.getCourse()) != null){
            returnResponses.add(new RestResponse(course.getCourse() + " already exists in database"));
        }else{
            em.persist(course);
            returnCourses.add(course);
        }
        List returnList = new ArrayList<Object>();
        returnList.add(returnCourses);
        returnList.add(returnResponses);
        return returnList;
    }

    @Transactional
    public List<Object> listUpdate(List<Course> courses) {
        List<Course> returnCourses = new ArrayList<Course>();
        List<RestResponse> returnResponses = new ArrayList<RestResponse>();
        for(Course course:courses)
            if(em.find(Course.class, course.getCourse()) == null){
                returnResponses.add(new RestResponse(course.getCourse() + " does not exist in database"));
            }else{
                em.merge(course);
                returnCourses.add(course);
            }
        List returnList = new ArrayList<Object>();
        returnList.add(returnCourses);
        returnList.add(returnResponses);
        return returnList;
    }
}