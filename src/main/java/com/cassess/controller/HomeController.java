package com.cassess.controller;

import com.cassess.service.ApiService;
import com.cassess.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
public class HomeController {

    //Create services
    @Autowired
    private ApiService apiService;

    @Autowired
    private DataBaseService dataBaseService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/resource")
    public Map<String, Object> homeResource(){
        Map<String, Object> model = new HashMap<>();
        ///return content (team member names)  and id for response
        model.put("id", UUID.randomUUID().toString());
        model.put("content", apiService.getTeamMembers());
        model.put("teamIDs", apiService.getIds());
        model.put("token", dataBaseService.getTaigaToken());
        model.put("taigaID", dataBaseService.getTaigaID());
        model.put("Info", apiService.getUserInfo());
        return model;
    }

}
