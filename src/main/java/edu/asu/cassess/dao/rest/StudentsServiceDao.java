package edu.asu.cassess.dao.rest;

import edu.asu.cassess.model.Taiga.CourseList;
import edu.asu.cassess.model.Taiga.TeamNames;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.persist.repo.rest.StudentRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * REST API implementation for student-based services.
 * 
 * @author tjjohn1
 *
 */
@Component
@Transactional
public class StudentsServiceDao {

    @Autowired
    private StudentRepo studentRepo;

        protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adds this student to the database.
     * 
     * @param student the student to add
     * @return the student passed or message if student already exists
     */
    @Transactional
    public <T> Object create(Student student) {
        System.out.println("Got into create");
        if(studentRepo.findOne(student.getEmail()) != null){
            return new RestResponse(student.getEmail() + " already exists in database");
        }else{
            studentRepo.save(student);
            return student;
        }
    }

    /**
     * Update the database entry for this student with new information.
     * 
     * @param student the student to update
     * @return the student passed or message if no such student exists in database
     */
    @Transactional
    public <T> Object update(Student student) {
        if(studentRepo.findOne(student.getEmail()) != null){
            studentRepo.save(student);
            return student;
        }else{
            return new RestResponse(student.getEmail() + " does not exist in database");
        }
    }
    
    /**
     * Finds student in database by email address.
     * 
     * @param email the string (email address) to search
     * @return the student or a message if the student is not found
     */
    @Transactional
    public <T> Object find(String email) {
        Student student = studentRepo.findOne(email);
        if(student != null){
            return student;
        }else{
            return new RestResponse(email + " does not exist in database");
        }
    }


    /**
     * Delete the student record from the database associated with this email address.
     * 
     * @param email the string (email address) used to find student
     * @return a message indicating success or no such database object
     */
    @Transactional
    public <T> Object delete(String email) {
        Student student = studentRepo.findOne(email);
        if(student != null){
            studentRepo.delete(student);
            return new RestResponse(email + " has been removed from the database");
        }else{
            return new RestResponse(email + " does not exist in the database");
        }
    }

    /**
     * List of all students from database.
     * 
     * @return List of all Student objects
     * @throws DataAccessException
     */
    public List<Student> listReadAll() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students", Student.class);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    /**
     * List of all students on the given team.
     * 
     * @param team_name name of the team to filter by
     * @return List of Student objects where student team is team_name
     * @throws DataAccessException
     */
    public List<Student> listReadByTeam(String team_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE team_name = ?1", Student.class);
        query.setParameter(1, team_name);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    /**
     * Adds students to the database.
     * 
     * @param students List of Student objects to add
     * @return JSONObject of student information and success or failure messages.
     */
    public JSONObject listCreate(List<Student> students) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for(Student student:students)
            if(studentRepo.findOne(student.getEmail()) != null){
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(student.getEmail() + " already exists in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else{
                studentRepo.save(student);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(student)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("success", successArray);
        returnJSON.put("failure", failureArray);
        return returnJSON;
    }

    /**
     * Update database records for students.
     * 
     * @param students List of Student objects to update
     * @return JSONObject of student information and success or failure messages.
     */
    public JSONObject listUpdate(List<Student> students) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Student student : students) {
            if (studentRepo.findOne(student.getEmail()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(student.getEmail() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                studentRepo.save(student);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(student)));
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
     * Deletes all student records from database associated with this team.
     * 
     * @param team_name name of team to filter by
     * @return RestResponse indicating success or failure
     */
    public <T> Object deleteByTeam(String team_name) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.students WHERE team_name = ?1 LIMIT 1", Student.class);
        preQuery.setParameter(1, team_name);
        Student student = (Student) preQuery.getSingleResult();
        if(student != null){
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.students WHERE team_name = ?1");
            query.setParameter(1, team_name);
            query.executeUpdate();
            return new RestResponse("All students in project " + team_name + " have been removed from the database");
        }else{
            return new RestResponse("No students in project " + team_name + " exist in the database");
        }
    }

    public List<CourseList> listGetCoursesForStudent(String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT course FROM cassess.students WHERE email = ?1", CourseList.class);
        query.setParameter(1, email);
                List<CourseList> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<TeamNames> listGetAssignedTeams(String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT team_name AS 'team' FROM cassess.students WHERE email = ?1", TeamNames.class);
        query.setParameter(1, email);
        List<TeamNames> resultList = query.getResultList();
        return resultList;
    }

}