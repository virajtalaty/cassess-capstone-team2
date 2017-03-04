package com.cassess.service;

public interface DataBaseService {

    public String getTaigaToken();

    public Long getTaigaID();

    public String getSlackTimeZone();

    public String getSlackTeamId();

    public int getGitHubCommitId();

    public String getGitHubCommitEmail();

    public String getProjectCreationDay();

    public String getTaigaProjectSlug();

}
