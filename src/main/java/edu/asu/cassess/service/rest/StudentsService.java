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

    @Override
    public <T> List<Student> listReadByTeam(String team_name) {
        return null;
    }

    public JSONObject listUpdate(List<Student> students){

        return studentsDao.listUpdate(students);

    }

    public JSONObject listCreate(List<Student> students){

        return studentsDao.listCreate(students);

    }

    public <T> Object deleteByTeam(String team_name){

        return studentsDao.deleteByTeam(team_name);

    }

}
