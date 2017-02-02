package com.cassess.controller;

import com.cassess.model.slack.ConsumeUsers;
import com.cassess.model.slack.UserObject;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@ImportResource({"classpath*:applicationContext.xml"})
public class HomeController {

    //private  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/resource")
    public Map<String, Object> home(){
        List<String> members = new ArrayList<>();
        Map<String, Object> model = new HashMap<>();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ConsumeUsers consumeUsers = (ConsumeUsers) context.getBean("consumeUsers");
        List<UserObject> users = consumeUsers.getUserList().getMembers();
        System.out.println("These are the people in the list: ");
        for ( UserObject member : users) {
            System.out.println(member.getName());
            String teamMember = member.getName();
            members.add(teamMember);
        }
        model.put("id", UUID.randomUUID().toString());
        model.put("content", members);
        return model;
    }
}
