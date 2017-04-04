package edu.asu.cassess.service.security;


import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.security.User;

import java.util.List;

public interface IUserService {

    <T> Object createUser(User userInput, long role);

    <T> Object createUsersByAdmins(List<Admin> admins);

    User adminUser(Admin admin);

    <T> Object createUsersByStudents(List<Student> students);

    User studentUser(Student student);

    <T> Object registerUser(String first_name, String family_name, String email, String password, boolean admin);
}
