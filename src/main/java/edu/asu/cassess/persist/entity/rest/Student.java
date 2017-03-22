package edu.asu.cassess.persist.entity.rest;

import javax.persistence.*;

@Entity
@Table(name="students")
public class Student {
    
    @Id
    @Column(name="email")
    private String email;

    @Column(name="full_name")
    private String full_name;

    @ManyToOne
    @JoinColumn(name = "course")
    private Course course;

    @Column(name="project_name")
    private String project_name;

    @Column(name="slack_team_id")
    private String slack_team_id;

    @Column(name="github_repo_id")
    private String github_repo_id;

    @Column(name="taiga_project_slug")
    private String taiga_project_slug;

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

    public Course getCourse() { return course; }

    public void setCourse(Course course) { this.course = course;}

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

    public String getTaiga_project_slug() {
        return taiga_project_slug;
    }

    public void setTaiga_project_slug(String taiga_project_slug) {
        this.taiga_project_slug = taiga_project_slug;
    }

}