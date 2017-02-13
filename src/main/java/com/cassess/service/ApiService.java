package com.cassess.service;

import java.util.*;

public interface ApiService {

    public List<String> getTeamMembers();

    public List<String> getIds();

    public List<String> getUserInfo();

    public String getGitHubCommitList();

    public String getTaskTotals();
}
