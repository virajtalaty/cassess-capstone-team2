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

    @Column(name="team")
    private String team;

    @Column(name="course")
    private String course;

    public GitHubPK() {
    }

    public GitHubPK(Date date, String username, String team, String course) {
        this.date = date;
        this.username = username;
        this.team = team;
        this.course = course;
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "GitHubPK{" +
                "date=" + date +
                ", username='" + username + '\'' +
                '}';
    }
}