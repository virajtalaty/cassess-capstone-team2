package com.cassess.controller;

import com.cassess.service.ApiService;
import com.cassess.service.DataBaseService;
import com.cassess.service.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by psharif on 3/15/17.
 */

@RestController
public class chartsController {


    //Create services
    @Autowired
    private ApiService apiService;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    SlackService slackService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/charts")
    public Map<String, Object> homeResource(){
        Map<String, Object> model = new HashMap<>();
        model.put("placeholder", "This is placeholder text");
        return model;
    }
}
