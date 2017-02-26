package com.cassess.service.DAO;

import com.cassess.entity.Student;
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
        System.out.println("Got into update");
        if(em.find(Student.class, student.getEmail()) != null){
            em.merge(student);
            return student;
        }else{
            return new RestResponse(student.getEmail() + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String email) {
        System.out.println("Got into email");
        Student student = em.find(Student.class, email);
        if(student != null){
            return student;
        }else{
            return new RestResponse(email + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String email) {
        System.out.println("Got into delete");
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
        System.out.println("Got into listReadAll");
        Query query = em.createNativeQuery("SELECT * FROM cassess.students", Student.class);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByCourse(String course) throws DataAccessException {
        System.out.println("Got into listReadByCourse");
        Query query = em.createNativeQuery("SELECT * FROM cassess.students WHERE course = ?1", Student.class);
        query.setParameter(1, course);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByProject(String project_name) throws DataAccessException {
        System.out.println("Got into listReadByProject");
        Query query = em.createNativeQuery("SELECT * FROM cassess.students WHERE project_name = ?1", Student.class);
        query.setParameter(1, project_name);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Object> listCreate(List<Student> students) {
        System.out.println("Got into listCreate");
        List<Student> returnStudents = new ArrayList<Student>();
        List<RestResponse> returnResponses = new ArrayList<RestResponse>();
        for(Student student:students)
            if(em.find(Student.class, student.getEmail()) != null){
                returnResponses.add(new RestResponse(student.getEmail() + " already exists in database"));
            }else{
                em.persist(student);
                returnStudents.add(student);
            }
        List returnList = new ArrayList<Object>();
        returnList.add(returnStudents);
        returnList.add(returnResponses);
        return returnList;
    }

    @Transactional
    public List<Object> listUpdate(List<Student> students) {
        System.out.println("Got into listUpdate");
        List<Student> returnStudents = new ArrayList<Student>();
        List<RestResponse> returnResponses = new ArrayList<RestResponse>();
        for(Student student:students)
            if(em.find(Student.class, student.getEmail()) == null){
                returnResponses.add(new RestResponse(student.getCourse() + " does not exist in database"));
            }else{
                em.merge(student);
                returnStudents.add(student);
            }
        List returnList = new ArrayList<Object>();
        returnList.add(returnStudents);
        returnList.add(returnResponses);
        return returnList;
    }

    @Transactional
    public <T> Object deleteByCourse(String course) {
        Query preQuery = em.createNativeQuery("SELECT * FROM cassess.students WHERE course = ?1 LIMIT 1", Student.class);
        preQuery.setParameter(1, course);
        Student student = (Student) preQuery.getSingleResult();
        System.out.println("Got into deleteByCourse: " + student.getCourse());
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
        System.out.println("Got into deleteByProject");
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