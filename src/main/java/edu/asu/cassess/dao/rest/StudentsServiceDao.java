package edu.asu.cassess.dao.rest;

import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.persist.repo.rest.StudentRepo;
import edu.asu.cassess.service.security.UserService;
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

    public <T> Object create(Student student) {
        System.out.println("Got into create");
        if(studentRepo.findOne(student.getEmail()) != null){
            return new RestResponse(student.getEmail() + " already exists in database");
        }else{
            studentRepo.save(student);
            return student;
        }
    }

    public <T> Object update(Student student) {
        if(studentRepo.findOne(student.getEmail()) != null){
            studentRepo.save(student);
            return student;
        }else{
            return new RestResponse(student.getEmail() + " does not exist in database");
        }
    }

    public <T> Object find(String email) {
        Student student = studentRepo.findOne(email);
        if(student != null){
            return student;
        }else{
            return new RestResponse(email + " does not exist in database");
        }
    }

    public <T> Object delete(String email) {
        Student student = studentRepo.findOne(email);
        if(student != null){
            studentRepo.delete(student);
            return new RestResponse(email + " has been removed from the database");
        }else{
            return new RestResponse(email + " does not exist in the database");
        }
    }

    public List<Student> listReadAll() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students", Student.class);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    public List<Student> listReadByTeam(String team_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE team_name = ?1", Student.class);
        query.setParameter(1, team_name);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

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

}