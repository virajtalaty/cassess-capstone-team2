package com.cassess.service;

import com.cassess.entity.Student;
import com.cassess.service.DAO.StudentsServiceDaoImpl;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import java.util.List;

@Service
public class StudentsService implements IStudentsService{

    @EJB
    private StudentsServiceDaoImpl studentsDao;

    public <T> Object create(Student student){

        return studentsDao.create(student);
    }

    public <T> Object update(Student student){

        return studentsDao.update(student);
    }

    public <T> Object find(String email){

        return studentsDao.find(email);
    }

    public <T> Object delete(String email){

        return studentsDao.delete(email);

    }

    public <T> List<Student> listReadAll(){

        return studentsDao.listReadAll();

    }

    public <T> List<Student> listReadByCourse(String project_name){

        return studentsDao.listReadByCourse(project_name);

    }

    public <T> List<Student> listReadByProject(String project_name){

        return studentsDao.listReadByProject(project_name);

    }

    public <T> List<Object> listUpdate(List<Student> students){

        return studentsDao.listUpdate(students);

    }

    public <T> List<Object> listCreate(List<Student> students){

        return studentsDao.listCreate(students);

    }

    public <T> Object deleteByCourse(String course){

        return studentsDao.delete(course);

    }

    public <T> Object deleteByProject(String project_name){

        return studentsDao.delete(project_name);

    }

}
