package com.cassess.controller;

import com.cassess.service.ApiService;
import com.cassess.service.DataBaseService;
import com.cassess.service.SlackService;
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

    @Autowired
    SlackService slackService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/resource")
    public Map<String, Object> homeResource(){
        Map<String, Object> model = new HashMap<>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", apiService.getTeamMembers());
        model.put("SlackService", slackService.getAllUsers());
        model.put("teamIDs", apiService.getIds());
        model.put("token", dataBaseService.getTaigaToken());
        model.put("taigaID", dataBaseService.getTaigaID());
        model.put("Info", apiService.getUserInfo());
        model.put("Slug", dataBaseService.getTaigaProjectSlug());
        model.put("TaskTotals", apiService.getTaskTotals());
        //model.put("ProjectCreate", dataBaseService.getProjectCreationDay());
        model.put("slackTeamId", dataBaseService.getSlackTeamId());
        model.put("slackTimeZone", dataBaseService.getSlackTimeZone());
        model.put("gitHubCommitId", dataBaseService.getGitHubCommitId());
        model.put("gitHubEmail", dataBaseService.getGitHubCommitEmail());
        model.put("gitHubCommitList", apiService.getGitHubCommitList());
        return model;
    }

}
