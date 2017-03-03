package com.cassess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="students")
public class Student {
    
    @Id
    @Column(name="email")
    private String email;

    @Column(name="full_name")
    private String full_name;

    @Column(name="course")
    private String course;

    @Column(name="project_name")
    private String project_name;

    @Column(name="slack_team_id")
    private String slack_team_id;

    @Column(name="github_repo_id")
    private String github_repo_id;

    @Column(name="taiga_project_id")
    private String taiga_project_id;

    public Student(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getSlack_team_id() {
        return slack_team_id;
    }

    public void setSlack_team_id(String slack_team_id) {
        this.slack_team_id = slack_team_id;
    }

    public String getGithub_repo_id() {
        return github_repo_id;
    }

    public void setGithub_repo_id(String github_repo_id) {
        this.github_repo_id = github_repo_id;
    }

    public String getTaiga_project_id() {
        return taiga_project_id;
    }

    public void setTaiga_project_id(String taiga_project_id) {
        this.taiga_project_id = taiga_project_id;
    }

}