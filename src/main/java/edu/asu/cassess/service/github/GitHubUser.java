package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUser {
    private String email;

    public GitHubUser(){

    }

    public GitHubUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "GitHubUser{" +
                "email='" + email + '\'' +
                '}';
    }
}
