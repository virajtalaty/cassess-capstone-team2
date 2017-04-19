package edu.asu.cassess.persist.entity.github;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="github_weight")
public class GitHubWeight {
    @EmbeddedId
    private GitHubPK gitHubPK;

    @Column(name="weight")
    private int weight;

    @Column(name ="email")
    private String email;

    public GitHubWeight(){

    }

    public GitHubWeight(String email, Date date, int weight, String username) {
        gitHubPK = new GitHubPK(date, username);
        this.email = email;
        this.weight = weight;
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

    @Override
    public String toString() {
        return "GitHubWeight{" +
                "gitHubPK=" + gitHubPK +
                ", weight=" + weight +
                ", email='" + email + '\'' +
                '}';
    }
}
