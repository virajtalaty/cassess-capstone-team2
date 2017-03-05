package com.cassess.service;

import com.cassess.model.github.GitHubCommitDataDao;
import com.cassess.model.taiga.AuthUserQueryDao;
import com.cassess.model.taiga.ProjectQueryDao;
import com.cassess.model.taiga.TaskTotalsQueryDaoImpl;
import com.cassess.service.DAO.SlackServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Autowired
    private AuthUserQueryDao authUserQueryDao;

    @Autowired
    private SlackServiceDAO slackServiceDAO;

    @Autowired
    private GitHubCommitDataDao gitHubCommitDataDao;

    @Autowired
    private ProjectQueryDao projectQueryDao;

    @Override
    public String getTaigaToken(){
        return authUserQueryDao.getUser("taigaTestUser").getAuth_token();
    }

    @Override
    public Long getTaigaID(){
        return  authUserQueryDao.getUser("taigaTestUser").getId();
    }

    @Override
    public String getSlackTimeZone(){
        return slackServiceDAO.getUser("U2G79FELT").getTz_label();
    }

    @Override
    public String getSlackTeamId(){
        return slackServiceDAO.getUser("U2G79FELT").getTeam_id();
    }

    @Override
    public String getProjectCreationDay(){
        return projectQueryDao.getProject().getRetrievalDate();
    }

    @Override
    public String getTaigaProjectSlug(){
        return projectQueryDao.getProject().getSlug();
    }


}
