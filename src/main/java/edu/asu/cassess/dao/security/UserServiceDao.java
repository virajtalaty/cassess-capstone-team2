package edu.asu.cassess.dao.security;

import edu.asu.cassess.dao.UserQueryDao;
import edu.asu.cassess.persist.entity.UserID;
import edu.asu.cassess.persist.entity.registerUser;
import edu.asu.cassess.persist.entity.rest.*;
import edu.asu.cassess.persist.entity.security.Authority;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.entity.security.UsersAuthority;
import edu.asu.cassess.persist.repo.AuthorityRepo;
import edu.asu.cassess.persist.repo.UserRepo;
import edu.asu.cassess.persist.repo.UsersAuthorityRepo;
import edu.asu.cassess.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UserServiceDao {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private AuthorityRepo authorityRepo;

    @Autowired
    private UsersAuthorityRepo usersAuthorityRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService usersService;

    @Autowired
    private UserQueryDao userQuery;

    @Autowired
    protected List<Object> userCreateList;

    @Transactional
    public <T> Object createUser(User userInput, long role){
        User user = userRepo.findByEmail(userInput.getEmail());
        if(user == null){
            userRepo.save(userInput);
            UsersAuthority usersAuth = new UsersAuthority(userInput.getId(), role);
            usersAuthorityRepo.save(usersAuth);
            return userInput;
        }else{
            return new RestResponse("User " + userInput.getEmail() + " already exists in database");
        }
    }

    @Transactional
    public <T> Object createUsersByAdmins(List<Admin> admins){

        for(Admin admin:admins){
            User user = usersService.adminUser(admin);
            long role = 1;
            Object userCreate = usersService.createUser(user, role);
            userCreateList.add(userCreate);
        }
        return userCreateList;
    }

    @Transactional
    public User adminUser(Admin admin){
        System.out.println("--------------------!!!!!!!!!!!!!!!!!!!!!!------------Got into AdminUser");
        String array[] = admin.getFull_name().split("\\s+");
        UserID userID = userQuery.getUserID();
        User user = new User(array[0], array[1], admin.getEmail(), null, "en", null, admin.getEmail(), passwordEncoder.encode(admin.getPassword()), null, true, (long) userID.getMax() + 1);
        return user;
    }

    @Transactional
    public <T> Object createUsersByStudents(List<Student> students){
        for(Student student:students){
            User user = usersService.studentUser(student);
            long role = 2;
            Object userCreate = usersService.createUser(user, role);
            userCreateList.add(userCreate);
        }
        return userCreateList;
    }

    @Transactional
    public User studentUser(Student student){
        System.out.println("--------------------!!!!!!!!!!!!!!!!!!!!!!------------Got into StudentUser");
        String array[] = student.getFull_name().split("\\s+");
        UserID userID = userQuery.getUserID();
        User user = new User(array[0], array[1], student.getEmail(), null, "en", null, student.getEmail(), passwordEncoder.encode(student.getPassword()), null, true, (long) userID.getMax() + 1);
        return user;
    }

    @Transactional
    public <T> Object registerUser(String first_name, String family_name, String email, String password, boolean admin){
        registerUser register_user = new registerUser(first_name, family_name, email, email, password);
        User user = new User(register_user.getFirstName(), register_user.getFamilyName(), register_user.getLogin(), register_user.getPhone(), register_user.getLanguage(), register_user.getPictureId(), register_user.getLogin(), register_user.getPassword(), register_user.getBirthDate(), register_user.getEnabled());
        Authority auth = new Authority();
        UserID userID = userQuery.getUserID();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId((long) userID.getMax() + 1);
        User userFind = userRepo.findByEmail(user.getEmail());
        if(userFind == null){
            userRepo.save(user);
            long role = 0;
            if(admin == true){
                role = 1;
            }if(admin == false){
                role = 2;
            }
            if(!authorityRepo.exists(role)){
                if(role == 1){
                    auth.setId((long) 1);
                    auth.setName("admin");
                    authorityRepo.save(auth);
                }else if(role == 2){
                    auth.setId((long) 2);
                    auth.setName("user");
                    authorityRepo.save(auth);
                }
            }
            UsersAuthority usersAuth = new UsersAuthority(user.getId(), role);
            usersAuthorityRepo.save(usersAuth);
            return user;
        }else{
            return new RestResponse("User " + user.getEmail() + " already exists in database");
        }

    }

    @Transactional
    public void updateStudent(Student student, User user){
        String array[] = student.getFull_name().split("\\s+");
        user.setEmail(student.getEmail());
        user.setLogin(student.getEmail());
        user.setPassword(passwordEncoder.encode(student.getPassword()));
        user.setFirstName(array[0]);
        user.setFamilyName(array[1]);
        userRepo.save(user);
    }

    @Transactional
    public void updateAdmin(Admin admin, User user){
        String array[] = admin.getFull_name().split("\\s+");
        user.setEmail(admin.getEmail());
        user.setLogin(admin.getEmail());
        user.setPassword(passwordEncoder.encode(admin.getPassword()));
        user.setFirstName(array[0]);
        user.setFamilyName(array[1]);
        userRepo.save(user);
    }

    @Transactional
    public void deleteUser(User user){
        usersAuthorityRepo.delete(user.getId());
        userRepo.delete(user);
    }

    @Transactional
    public void courseDelete(Course course){
        for(Admin admin:course.getAdmins()){
            User user = userRepo.findByEmail(admin.getEmail());
            if(user != null)
            {
                usersService.deleteUser(user);
            }
        }
        for(Team team:course.getTeams()){
            for(Student student:team.getStudents()){
                User user = userRepo.findByEmail(student.getEmail());
                if(user != null)
                {
                    usersService.deleteUser(user);
                }
            }
        }
    }

    @Transactional
    public void courseUpdate(Course course) {
        for (Admin admin : course.getAdmins()) {
            User user = userRepo.findByEmail(admin.getEmail());
            if (user != null) {
                usersService.updateAdmin(admin, user);
            }
        }
        for (Team team : course.getTeams()) {
            for (Student student : team.getStudents()) {
                User user = userRepo.findByEmail(student.getEmail());
                if (user != null) {
                    usersService.updateStudent(student, user);
                }
            }
        }
    }

    @Transactional
    public void teamUpdate(Team team) {
        for (Student student : team.getStudents()) {
            User user = userRepo.findByEmail(student.getEmail());
            if (user != null) {
                usersService.updateStudent(student, user);
            }
        }
    }

    @Transactional
    public void teamDelete(Team team){
        for(Student student:team.getStudents()){
            User user = userRepo.findByEmail(student.getEmail());
            if(user != null)
            {
                usersService.deleteUser(user);
            }
        }
    }
}
