package edu.asu.cassess.persist.entity.github;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "commit_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitData {
    @EmbeddedId
    private GitHubPK gitHubPK;

    @Column(name = "lines_of_code_added")
    private int linesOfCodeAdded;

    @Column(name = "lines_of_code_deleted")
    private int linesOfCodeDeleted;

    @Column(name = "commits")
    private int commits;

    @Column(name = "project_name")
    private String projectName;

    @Column(name="github_owner")
    private String gitHubOwner;

    @Column(name="email")
    private String email;

    @Column(name="course")
    private String course;

    @Column(name="team")
    private String team;

    public CommitData() {
    }

    public CommitData(Date date, String username, String email, int linesOfCodeAdded, int linesOfCodeDeleted, int commits, String projectName, String gitHubOwner, String course, String team) {
        this.gitHubPK = new GitHubPK(date, username);
        this.email = email;
        this.linesOfCodeAdded = linesOfCodeAdded;
        this.linesOfCodeDeleted = linesOfCodeDeleted;
        this.commits = commits;
        this.projectName = projectName;
        this.gitHubOwner = gitHubOwner;
        this.course = course;
        this.team = team;
    }

    public GitHubPK getGitHubPK() {
        return gitHubPK;
    }

    public void setGitHubPK(GitHubPK gitHubPK) {
        this.gitHubPK = gitHubPK;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "CommitData{" +
                "gitHubPK=" + gitHubPK +
                ", linesOfCodeAdded=" + linesOfCodeAdded +
                ", linesOfCodeDeleted=" + linesOfCodeDeleted +
                ", commits=" + commits +
                ", projectName='" + projectName + '\'' +
                ", gitHubOwner='" + gitHubOwner + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}