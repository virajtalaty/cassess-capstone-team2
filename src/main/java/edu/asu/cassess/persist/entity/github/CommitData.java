package edu.asu.cassess.persist.entity.github;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="commit_data")
public class CommitData{
    @EmbeddedId
    private CommitDataPK commitDataPK;

    @Column(name="lines_of_code_added")
    private int linesOfCodeAdded;

    @Column(name="lines_of_code_deleted")
    private int linesOfCodeDeleted;

    @Column(name="commits")
    private int commits;

    @Column(name="project_name")
    private String projectName;

    @Column(name="github_owner")
    private String gitHubOwner;

    @Column(name="email")
    private String email;

    public CommitData() {
    }

    public CommitData(Date date, String username, String email, int linesOfCodeAdded, int linesOfCodeDeleted, int commits, String projectName, String gitHubOwner) {
        this.commitDataPK = new CommitDataPK(date, username);
        this.email = email;
        this.linesOfCodeAdded = linesOfCodeAdded;
        this.linesOfCodeDeleted = linesOfCodeDeleted;
        this.commits = commits;
        this.projectName = projectName;
        this.gitHubOwner = gitHubOwner;
    }

    public CommitDataPK getCommitDataPK() {
        return commitDataPK;
    }

    public void setCommitDataPK(CommitDataPK commitDataPK) {
        this.commitDataPK = commitDataPK;
    }

    public int getLinesOfCodeAdded() {
        return linesOfCodeAdded;
    }

    public void setLinesOfCodeAdded(int linesOfCodeAdded) {
        this.linesOfCodeAdded = linesOfCodeAdded;
    }

    public int getLinesOfCodeDeleted() {
        return linesOfCodeDeleted;
    }

    public void setLinesOfCodeDeleted(int linesOfCodeDeleted) {
        this.linesOfCodeDeleted = linesOfCodeDeleted;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGitHubOwner() {
        return gitHubOwner;
    }

    public void setGitHubOwner(String gitHubOwner) {
        this.gitHubOwner = gitHubOwner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CommitData{" +
                "commitDataPK=" + commitDataPK +
                ", linesOfCodeAdded=" + linesOfCodeAdded +
                ", linesOfCodeDeleted=" + linesOfCodeDeleted +
                ", commits=" + commits +
                ", projectName='" + projectName + '\'' +
                ", gitHubOwner='" + gitHubOwner + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}