package com.cassess.dao.rest;

import com.cassess.entity.rest.Student;
import com.cassess.entity.rest.RestResponse;
import com.cassess.entity.taiga.Slugs;
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
import java.util.List;

@Component
public class StudentsServiceDaoImpl extends StudentsServiceDao {

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
    public <T> Object create(Student student) {
        System.out.println("Got into create");
        if(em.find(Student.class, student.getEmail()) != null){
            return new RestResponse(student.getEmail() + " already exists in database");
        }else{
            em.persist(student);
            return student;
        }
    }

    @Transactional
    public <T> Object update(Student student) {
        if(em.find(Student.class, student.getEmail()) != null){
            em.merge(student);
            return student;
        }else{
            return new RestResponse(student.getEmail() + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String email) {
        Student student = em.find(Student.class, email);
        if(student != null){
            return student;
        }else{
            return new RestResponse(email + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String email) {
        Student student = em.find(Student.class, email);
        if(student != null){
            em.remove(student);
            return new RestResponse(email + " has been removed from the database");
        }else{
            return new RestResponse(email + " does not exist in the database");
        }
    }

    @Transactional
    public List<Student> listReadAll() throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM cassess.students", Student.class);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByCourse(String course) throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM cassess.students WHERE course = ?1", Student.class);
        query.setParameter(1, course);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByProject(String project_name) throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM cassess.students WHERE project_name = ?1", Student.class);
        query.setParameter(1, project_name);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadBySlug(String slug) throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM cassess.students WHERE taiga_project_slug = ?1", Student.class);
        query.setParameter(1, slug);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Slugs> listGetSlugs(String course) throws DataAccessException {
        Query query = em.createNativeQuery("SELECT taiga_project_slug FROM cassess.students WHERE course = ?1", Slugs.class);
        query.setParameter(1, course);
        List<Slugs> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public JSONObject listCreate(List<Student> students) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for(Student student:students)
            if(em.find(Student.class, student.getEmail()) != null){
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(student.getEmail() + " already exists in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else{
                em.persist(student);
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
            if (em.find(Student.class, student.getEmail()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(student.getEmail() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                em.merge(student);
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
        Query preQuery = em.createNativeQuery("SELECT * FROM cassess.students WHERE course = ?1 LIMIT 1", Student.class);
        preQuery.setParameter(1, course);
        Student student = (Student) preQuery.getSingleResult();
        if(student != null){
            Query query = em.createNativeQuery("DELETE FROM cassess.students WHERE course = ?1");
            query.setParameter(1, course);
            query.executeUpdate();
            return new RestResponse("All students in course " + course + " have been removed from the database");
        }else{
            return new RestResponse("No students in course " + course + " exist in the database");
        }
    }

    @Transactional
    public <T> Object deleteByProject(String project_name) {
        Query preQuery = em.createNativeQuery("SELECT * FROM cassess.students WHERE project_name = ?1 LIMIT 1", Student.class);
        preQuery.setParameter(1, project_name);
        Student student = (Student) preQuery.getSingleResult();
        if(student != null){
            Query query = em.createNativeQuery("DELETE FROM cassess.students WHERE project_name = ?1");
            query.setParameter(1, project_name);
            query.executeUpdate();
            return new RestResponse("All students in project " + project_name + " have been removed from the database");
        }else{
            return new RestResponse("No students in project " + project_name + " exist in the database");
        }
    }

}