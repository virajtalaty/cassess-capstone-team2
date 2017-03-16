package com.cassess.service;

import com.cassess.entity.taiga.MemberData;
import com.cassess.entity.taiga.Project;

import java.util.List;

public interface DataBaseService {

    public String getTaigaToken();

    public Long getTaigaID();

    public String getSlackTimeZone();

    public String getSlackTeamId();

    public List<MemberData> getTaigaMembers();

    public List<Project> getTaigaProjects();

}
