package edu.asu.cassess.persist.entity.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="courses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    @Id
    @Column(name = "course")
    private String course;

    @Column(name = "owner")
    private String owner;

    @Column(name = "slack_token")
    private String slack_token;

    @Column(name = "github_token")
    private String github_token;

    @Column(name = "taiga_token")
    private String taiga_token;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
    private List<Team> teams;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
    private List<Admin> admins;

    public Course() {

    }

    public Course(String course, String owner, String slack_token, String github_token, String taiga_token, List<Student> students, List<Team> teams) {
        this.course = course;
        this.owner = owner;
        this.slack_token = slack_token;
        this.github_token = github_token;
        this.taiga_token = taiga_token;
        this.teams = teams;
        this.admins = admins;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSlack_token() {
        return slack_token;
    }

    public void setSlack_token(String slack_token) {
        this.slack_token = slack_token;
    }

    public String getGithub_token() {
        return github_token;
    }

    public void setGithub_token(String github_token) {
        this.github_token = github_token;
    }

    public String getTaiga_token() {
        return taiga_token;
    }

    public void setTaiga_token(String taiga_token) {
        this.taiga_token = taiga_token;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        for(Team team:teams){
            team.setCourse(course);
        }
        this.teams = teams;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        for(Admin admin:admins){
            admin.setCourse(course);
        }
        this.admins = admins;
    }


}