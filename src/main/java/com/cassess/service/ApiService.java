package com.cassess.service;

import java.util.*;

public interface ApiService {

    //Gets the names of team Members from Slack
    public List<String> getTeamMembers();

    //Get the slack team Ids
    public List<String> getIds();

    public List<String> getUserInfo();
}
