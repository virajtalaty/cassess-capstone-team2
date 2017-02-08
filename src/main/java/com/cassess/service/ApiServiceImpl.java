package com.cassess.service;

import com.cassess.model.slack.ConsumeUsers;
import com.cassess.model.slack.UserObject;
import com.cassess.model.taiga.AuthUserQueryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class ApiServiceImpl implements ApiService{

    private List<UserObject> users;

    @Autowired
    private ConsumeUsers consumeUsers;

    @Autowired
    private AuthUserQueryDao authUserQueryDao;


    ApiServiceImpl(){
    }

    @Override
    public List<String> getTeamMembers(){
        List<String> members = new ArrayList<>();
        users = consumeUsers.getUserList().getMembers();
        // Add the names of the members to a list
        for ( UserObject member : users) {
            members.add( member.getName());
        }
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

    @Override
    public List<String> getUserInfo(){
        List<String> Info = new ArrayList<>();

        Info.add(authUserQueryDao.getUser("TaigaTestUser@gmail.com").getFull_name());
        Info.add(authUserQueryDao.getUser("TaigaTestUser@gmail.com").getEmail());

        return Info;
    }

}
