package com.cassess.controller;

import com.cassess.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
public class HomeController {

    //Create a User Service
    @Autowired
    private UserService userService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/resource")
    public Map<String, Object> home(){
        Map<String, Object> model = new HashMap<>();
        ///return content (team member names)  and id for response
        model.put("id", UUID.randomUUID().toString());
        model.put("content", userService.getTeamMembers());
        model.put("teamIDs", userService.getIds());
        return model;
    }
}
