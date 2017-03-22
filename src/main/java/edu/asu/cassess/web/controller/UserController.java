package edu.asu.cassess.web.controller;

import edu.asu.cassess.dao.UserQueryDao;
import edu.asu.cassess.persist.entity.UserID;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.entity.registerUser;
import edu.asu.cassess.persist.repo.UserRepo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@Api(description = "Users management API")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserQueryDao userQuery;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody List<User> usersList() {
        logger.debug("get users list");
        return userRepo.findAll();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public @ResponseBody User getUser(@PathVariable Long userId) {
        logger.debug("get user");
        return userRepo.findOne(userId);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody User saveUser(@RequestHeader(name = "first_name", required = true) String first_name,
                                       @RequestHeader(name = "family_name", required = true) String family_name,
                                       @RequestHeader(name = "email", required = true) String email,
                                       @RequestHeader(name = "phone", required = true) String phone,
                                       @RequestHeader(name = "birth_date", required = true) Date birth_date,
                                       @RequestHeader(name = "login", required = true) String login,
                                       @RequestHeader(name = "password", required = true) String password)  {
        registerUser register_user = new registerUser(first_name, family_name, email, phone, login, password, birth_date);
        User user = new User(register_user.getFirstName(), register_user.getFamilyName(), register_user.getEmail(), register_user.getPhone(), register_user.getLanguage(), register_user.getPictureId(), register_user.getLogin(), register_user.getPassword(), register_user.getBirthDate(), register_user.getEnabled());
        UserID userID = userQuery.getUserID();
        logger.debug("save user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(userID.getMax() + 1);
        userRepo.save(user);
        return user;
    }


}

 
