package com.cassess.service;

import java.util.*;

public interface UserService {

    //Gets the names of team Members from Slack
    public List<String> getTeamMembers();

    //Get the channel name
    public List<String> getIds();
}
