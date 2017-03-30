package edu.asu.cassess.dao.rest;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.taiga.CourseList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import edu.asu.cassess.persist.repo.rest.CourseRepo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class CourseServiceDao {

    @Autowired
    private CourseRepo courseDao;

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    /**
     * Save this course to the database.
     * 
     * @param courseInput the Course object to save
     * @return courseInput or a message if the course is already in the database
     */
    @Transactional
    public <T> Object create(Course courseInput) {
        Course course = (Course) courseDao.findOne(courseInput.getCourse());
        if (course != null) {
            return new RestResponse(course.getCourse() + " already exists in database");
        } else {
            courseDao.save(courseInput);
            return courseInput;
        }
    }

    /**
     * Update this course in the database.
     * 
     * @param courseInput the Course to update
     * @return courseInput or a message indicating the course does not exist in the database
     */
    @Transactional
    public <T> Object update(Course courseInput) {
        Course course = (Course) courseDao.findOne(courseInput.getCourse());
        if (course != null) {
            courseDao.save(course);
            return courseInput;
        } else {
            return new RestResponse(courseInput + " does not exist in database");
        }
    }

    /**
     * Find a course in the database by its name.
     * 
     * @param courseName the name to be used to find the course
     * @return the Course object or a message indicating the course does not exist
     */
    @Transactional
    public <T> Object find(String courseName) {
        Course course = (Course) courseDao.findOne(courseName);
        if (course != null) {
            return course;
        } else {
            return new RestResponse(courseName + " does not exist in database");
        }
    }

    /**
     * Delete a course from the database by its name.
     * 
     * @param courseName the name to be used to find the course
     * @return a message indicating the course was deleted or not found
     */
    @Transactional
    public <T> Object delete(String courseName) {
        Course course = (Course) courseDao.findOne(courseName);
        if (course != null) {
            courseDao.delete(course);
            return new RestResponse(course + " has been removed from the database");
        } else {
            return new RestResponse(course + " does not exist in the database");
        }
    }

    /**
     * Fetch a list of all courses from database.
     * 
     * @return A List of Courses
     * @throws DataAccessException
     */
    @Transactional
    public List<Course> listRead() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.courses", Course.class);
        List<Course> resultList = query.getResultList();
        return resultList;
    }

    /**
     * Fetch course list from students table.
     * 
     * @return A List of CourseList
     * @throws DataAccessException
     */
    @Transactional
    public List<CourseList> listGetCourses() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT course AS 'course' FROM cassess.students", CourseList.class);
        List<CourseList> resultList = query.getResultList();
        return resultList;
    }
    
    /**
     * Create new courses in database from a List of Courses.
     * 
     * @param courses List of Courses
     * @return JSONObject of courses or messages if course(s) already exist
     */
    @Transactional
    public JSONObject listCreate(List<Course> courses) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Course course : courses) {
            if (courseDao.findOne(course.getCourse()) != null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " already exists in the database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                courseDao.save(course);
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

    /**
     * Update courses from a List.
     * 
     * @param courses List of Courses
     * @return JSONObject of course information or messages if course(s) do not exist
     */
    @Transactional
    public JSONObject listUpdate(List<Course> courses) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Course course : courses) {
            if (courseDao.findOne(course.getCourse()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(course.getCourse() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                courseDao.save(course);
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