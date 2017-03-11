package com.cassess.service;

import com.cassess.dao.taiga.MemberQueryDaoImpl;
import com.cassess.dao.taiga.MembersDaoImpl;
import com.cassess.entity.taiga.MemberData;
import com.cassess.entity.taiga.Project;
import com.cassess.model.github.GitHubCommitDataDao;
import com.cassess.dao.taiga.AuthUserQueryDao;
import com.cassess.dao.taiga.ProjectQueryDao;
import com.cassess.service.DAO.SlackServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Autowired
    private MemberQueryDaoImpl memberQueryDao;

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
    public List<MemberData> getTaigaMembers(){
        return memberQueryDao.getMembers("Product Owner");
    }

    @Override
    public List<Project> getTaigaProjects(){
        return projectQueryDao.getProjects();
    }


}
