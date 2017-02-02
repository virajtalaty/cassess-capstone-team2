package com.cassess.model.github;

public class GitHubApplication {

    public static void main(String[] args){
        GatherGitHubData gitHubData = new GatherGitHubData();
        gitHubData.fetchData();
    }
}
