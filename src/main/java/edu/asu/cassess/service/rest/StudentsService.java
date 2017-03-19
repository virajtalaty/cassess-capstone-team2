package edu.asu.cassess.service.rest;

import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.dao.rest.StudentsServiceDao;
import edu.asu.cassess.persist.entity.taiga.Slugs;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import java.util.List;

@Service
public class StudentsService implements IStudentsService {

    @EJB
    private StudentsServiceDao studentsDao;

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

    public <T> List<Student> listReadByCourse(String course){

        return studentsDao.listReadByCourse(course);

    }

    public <T> List<Student> listReadByProject(String project_name){

        return studentsDao.listReadByProject(project_name);

    }

    public JSONObject listUpdate(List<Student> students){

        return studentsDao.listUpdate(students);

    }

    public JSONObject listCreate(List<Student> students){

        return studentsDao.listCreate(students);

    }

    public <T> Object deleteByCourse(String course){

        return studentsDao.deleteByCourse(course);

    }

    public <T> Object deleteByProject(String project_name){

        return studentsDao.deleteByProject(project_name);

    }

    public List<Slugs> listGetSlugs(String course) {

        return studentsDao.listGetSlugs(course);
    }

}
