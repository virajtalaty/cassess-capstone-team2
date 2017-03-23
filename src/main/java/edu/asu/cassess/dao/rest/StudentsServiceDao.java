package edu.asu.cassess.dao.rest;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.entity.taiga.Slugs;
import edu.asu.cassess.persist.entity.taiga.Teams;
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

@Component
@Transactional
public class StudentsServiceDao extends GenericDAOImpl<Student, Long> {

    @Autowired
    private StudentRepo studentDao;

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public <T> Object create(Student student) {
        System.out.println("Got into create");
        if(studentDao.findOne(student.getEmail()) != null){
            return new RestResponse(student.getEmail() + " already exists in database");
        }else{
            studentDao.save(student);
            return student;
        }
    }

    @Transactional
    public <T> Object update(Student student) {
        if(studentDao.findOne(student.getEmail()) != null){
            studentDao.save(student);
            return student;
        }else{
            return new RestResponse(student.getEmail() + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String email) {
        Student student = studentDao.findOne(email);
        if(student != null){
            return student;
        }else{
            return new RestResponse(email + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String email) {
        Student student = studentDao.findOne(email);
        if(student != null){
            studentDao.delete(student);
            return new RestResponse(email + " has been removed from the database");
        }else{
            return new RestResponse(email + " does not exist in the database");
        }
    }

    @Transactional
    public List<Student> listReadAll() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students", Student.class);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE course = ?1", Student.class);
        query.setParameter(1, course);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByProject(String project_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE project_name = ?1", Student.class);
        query.setParameter(1, project_name);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadBySlug(String slug) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.students WHERE taiga_project_slug = ?1", Student.class);
        query.setParameter(1, slug);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Slugs> listGetSlugs(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT taiga_project_slug FROM cassess.students WHERE course = ?1", Slugs.class);
        query.setParameter(1, course);
        List<Slugs> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Teams> listGetProjectNames(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT project_name AS 'team' FROM cassess.students WHERE course = ?1", Teams.class);
        query.setParameter(1, course);
        List<Teams> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public JSONObject listCreate(List<Student> students) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for(Student student:students)
            if(studentDao.findOne(student.getEmail()) != null){
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(student.getEmail() + " already exists in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else{
                studentDao.save(student);
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

    @Transactional
    public JSONObject listUpdate(List<Student> students) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Student student : students) {
            if (studentDao.findOne(student.getEmail()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(student.getEmail() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                studentDao.save(student);
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

    @Transactional
    public <T> Object deleteByCourse(String course) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.students WHERE course = ?1 LIMIT 1", Student.class);
        preQuery.setParameter(1, course);
        Student student = (Student) preQuery.getSingleResult();
        if(student != null){
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.students WHERE course = ?1");
            query.setParameter(1, course);
            query.executeUpdate();
            return new RestResponse("All students in course " + course + " have been removed from the database");
        }else{
            return new RestResponse("No students in course " + course + " exist in the database");
        }
    }

    @Transactional
    public <T> Object deleteByProject(String project_name) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.students WHERE project_name = ?1 LIMIT 1", Student.class);
        preQuery.setParameter(1, project_name);
        Student student = (Student) preQuery.getSingleResult();
        if(student != null){
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.students WHERE project_name = ?1");
            query.setParameter(1, project_name);
            query.executeUpdate();
            return new RestResponse("All students in project " + project_name + " have been removed from the database");
        }else{
            return new RestResponse("No students in project " + project_name + " exist in the database");
        }
    }

}