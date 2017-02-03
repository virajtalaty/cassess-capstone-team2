package com.cassess.service;

import com.cassess.model.slack.ConsumeUsers;
import com.cassess.model.slack.UserObject;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

//Uses

@Service
@ImportResource({"classpath*:applicationContext.xml"})
public class UserServiceImpl implements UserService {

    private List<UserObject> users;

    UserServiceImpl(){
    }

    @Override
    public List<String> getTeamMembers(){
        List<String> members = new ArrayList<>();
        // Load Beans from configuration file
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        // Get Bean from container
        ConsumeUsers consumeUsers = context.getBean("consumeUsers", ConsumeUsers.class);
        // Get a list of users using consumeUsers Methods
        users = consumeUsers.getUserList().getMembers();
        // Add the names of the members to a list
        for ( UserObject member : users) {
            members.add( member.getName());
        }
        //Close the Context
        context.close();

        //Return the list of members names
        return members;
    }

    @Override
    public List<String> getIds(){
        List<String> Ids = new ArrayList<>();
        for ( UserObject member : users) {
            Ids.add( member.getId());
        }
        return Ids;
    }


}
