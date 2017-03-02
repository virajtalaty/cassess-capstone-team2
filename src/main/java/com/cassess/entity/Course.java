package com.cassess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public Course() {

    }

    public Course(String course, String owner, String slack_token, String github_token, String taiga_token) {
        this.course = course;
        this.owner = owner;
        this.slack_token = slack_token;
        this.github_token = github_token;
        this.taiga_token = taiga_token;
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


}