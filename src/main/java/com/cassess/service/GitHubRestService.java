package com.cassess.service;

import com.cassess.service.DAO.GitHubRestServiceDaoImpl;
import com.cassess.model.github.GitHubRest;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;

@Service
public class GitHubRestService implements IGitHubRestService{

    @EJB
    private GitHubRestServiceDaoImpl gitHubAuthDao;

    public GitHubRest updateGitHubAuth(GitHubRest gitHubRest){

        if(gitHubRest == null){
            return null;
        }else{
            return gitHubAuthDao.save(gitHubRest);
        }
    }
}
