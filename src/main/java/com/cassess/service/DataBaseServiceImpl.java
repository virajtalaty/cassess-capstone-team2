package com.cassess.service;

import com.cassess.dao.taiga.MemberQueryDao;
import com.cassess.entity.taiga.MemberData;
import com.cassess.entity.taiga.Project;
import com.cassess.model.github.GitHubCommitDataDao;
import com.cassess.dao.taiga.IAuthQueryDao;
import com.cassess.dao.taiga.IProjectQueryDao;
import com.cassess.service.DAO.SlackServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Autowired
    private IAuthQueryDao IAuthQueryDao;

    @Autowired
    private SlackServiceDAO slackServiceDAO;

    @Autowired
    private GitHubCommitDataDao gitHubCommitDataDao;

    @Autowired
    private IProjectQueryDao IProjectQueryDao;

    @Autowired
    private MemberQueryDao memberQueryDao;

    @Override
    public String getTaigaToken(){
        return IAuthQueryDao.getUser("taigaTestUser").getAuth_token();
    }

    @Override
    public Long getTaigaID(){
        return  IAuthQueryDao.getUser("taigaTestUser").getId();
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
        return memberQueryDao.getMembers("Product Owner", "tjjohn1-ser-401-capstone-project-team-2");
    }

    @Override
    public List<Project> getTaigaProjects(){
        return IProjectQueryDao.getAllProjects();
    }


}
