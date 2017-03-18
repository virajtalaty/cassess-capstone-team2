package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Gathers sha (commit ID) from GitHub API path:
 * https://api.github.com/repos/:owner/:repositoryName/commits
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubSha {

    private String sha;

    public GitHubSha() {
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    @Override
    public String toString() {
        return "GitHubSha{" +
                "sha='" + sha + '\'' +
                '}';
    }
}