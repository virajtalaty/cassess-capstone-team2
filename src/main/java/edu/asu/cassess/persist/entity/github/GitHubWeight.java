package edu.asu.cassess.persist.entity.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="github_weight")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubWeight {
    @EmbeddedId
    private GitHubPK gitHubPK;

    @Column(name="weight")
    private int weight;

    @Column(name ="email")
    private String email;

    @Column(name="course")
    private String course;

    @Column(name="team")
    private String team;

    public GitHubWeight(){

    }

    public GitHubWeight(String email, Date date, int weight, String username, String course, String team) {
        gitHubPK = new GitHubPK(date, username);
        this.email = email;
        this.weight = weight;
        this.course = course;
        this.team = team;
    }

    public GitHubPK getGitHubPK() {
        return gitHubPK;
    }

    public void setGitHubPK(GitHubPK gitHubPK) {
        this.gitHubPK = gitHubPK;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
        return "GitHubWeight{" +
                "gitHubPK=" + gitHubPK +
                ", weight=" + weight +
                ", email='" + email + '\'' +
                '}';
    }
}
