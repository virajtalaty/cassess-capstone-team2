package edu.asu.cassess.service.security;

import edu.asu.cassess.dao.security.UserServiceDao;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.security.User;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import java.util.List;

@Service
public class UserService implements IUserService{

    @EJB
    private UserServiceDao userServiceDao;

    @Override
    public <T> Object createUser(User userInput, long role){
        return userServiceDao.createUser(userInput, role);
    }

    @Override
    public <T> Object createUsersByAdmins(List<Admin> admins){
        return userServiceDao.createUsersByAdmins(admins);
    }

    @Override
    public User adminUser(Admin admin){
        return userServiceDao.adminUser(admin);
    }

    @Override
    public <T> Object createUsersByStudents(List<Student> students){
        return userServiceDao.createUsersByStudents(students);
    }

    @Override
    public User studentUser(Student student){
        return userServiceDao.studentUser(student);
    }

    @Override
    public <T> Object registerUser(String first_name, String family_name, String email, String password, boolean admin){
        return userServiceDao.registerUser(first_name, family_name, email, password, admin);
    }

    @Override
    public void updateStudent(Student student, User user){

    }

    @Override
    public void updateAdmin(Admin admin, User user){

    }

    @Override
    public void deleteUser(User user){}

    @Override
    public void courseDelete(Course course){}

    @Override
    public void courseUpdate(Course course){}

    @Override
    public void teamUpdate(Team team){}

    @Override
    public void teamDelete(Team team){}


}
