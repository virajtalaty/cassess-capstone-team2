package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubBranches {
    private String name;

    public GitHubBranches(){
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GitHubBranches{" +
                "name='" + name + '\'' +
                '}';
    }
}
