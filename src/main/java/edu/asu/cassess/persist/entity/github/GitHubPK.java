package edu.asu.cassess.persist.entity.github;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Date;

@Embeddable
public class GitHubPK implements Serializable {

    @Column(name="date")
    protected Date date;

    @Column(name="username")
    protected String username;

    public GitHubPK() {
    }

    public GitHubPK(Date date, String username) {
        this.date = date;
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "GitHubPK{" +
                "date=" + date +
                ", username='" + username + '\'' +
                '}';
    }
}