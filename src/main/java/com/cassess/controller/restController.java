package com.cassess.controller;

import com.cassess.service.DAO.GitHubRestServiceDao;
import com.cassess.model.github.GitHubRest;
import com.cassess.service.IGitHubRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;

@RestController
@RequestMapping(value = "/rest")
public class restController {

    @EJB
    private GitHubRestServiceDao gitHubAuthDao;

    @Autowired
    private IGitHubRestService gitHubRestService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/github", method = RequestMethod.POST)
    public  @ResponseBody
    GitHubRest newGitHubAuth(@RequestBody GitHubRest gitHubRest){

        System.out.println("ID: " + gitHubRest.getId());
        System.out.println("Token: " + gitHubRest.getToken());
        if(gitHubRest == null){
            return null;
        }else{
            return gitHubRestService.updateGitHubAuth(gitHubRest);
        }
    }
}

