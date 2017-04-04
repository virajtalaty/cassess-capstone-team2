package edu.asu.cassess.service.rest;

import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.dao.rest.StudentsServiceDao;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import java.util.List;

@Service
public class StudentsService implements IStudentsService {

    @EJB
    private StudentsServiceDao studentsDao;

    @Override
    public <T> Object create(Student student){

        return studentsDao.create(student);
    }

    @Override
    public <T> Object update(Student student){

        return studentsDao.update(student);
    }

    @Override
    public <T> Object find(String email){

        return studentsDao.find(email);
    }

    @Override
    public <T> Object delete(String email){

        return studentsDao.delete(email);

    }

    @Override
    public <T> List<Student> listReadAll(){

        return studentsDao.listReadAll();

    }

    @Override
    public <T> List<Student> listReadByTeam(String team_name) {
        return studentsDao.listReadByTeam(team_name);
    }

    @Override
    public JSONObject listUpdate(List<Student> students){

        return studentsDao.listUpdate(students);

    }

    @Override
    public JSONObject listCreate(List<Student> students){

        return studentsDao.listCreate(students);

    }

    @Override
    public <T> Object deleteByTeam(String team_name){

        return studentsDao.deleteByTeam(team_name);

    }

}
