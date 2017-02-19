package com.cassess.controller;

import com.cassess.dao.GitHubAuthDaoImpl;
import com.cassess.model.github.GitHubAuth;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;

@RestController
@RequestMapping(value = "/github")
public class GitHubController {

    @EJB
    private GitHubAuthDaoImpl gitHubAuthDao;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public  @ResponseBody GitHubAuth newGitHubAuth( @RequestBody GitHubAuth gitHubAuth){

        System.out.println("ID: " + gitHubAuth.getId());
        System.out.println("Token: " + gitHubAuth.getToken());
        if(gitHubAuth == null){
            return null;
        }else{
            return gitHubAuthDao.save(gitHubAuth);
        }
    }
}

