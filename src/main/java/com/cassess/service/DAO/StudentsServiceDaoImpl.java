package com.cassess.service.DAO;

import com.cassess.entity.Course;
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
        Query query = em.createNativeQuery("SELECT * FROM students");
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByCourse(String course) throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM students WHERE course = ?1");
        query.setParameter(1, course);
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Student> listReadByProject(String project_name) throws DataAccessException {
        Query query = em.createNativeQuery("SELECT * FROM students WHERE project_name = ?1");
        List<Student> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Object> listCreate(List<Student> students) {
        List<Student> returnStudents = new ArrayList<Student>();
        List<RestResponse> returnResponses = new ArrayList<RestResponse>();
        for(Student student:students)
            if(em.find(Course.class, student.getEmail()) != null){
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
        List<Student> returnStudents = new ArrayList<Student>();
        List<RestResponse> returnResponses = new ArrayList<RestResponse>();
        for(Student student:students)
            if(em.find(Course.class, student.getEmail()) == null){
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


}