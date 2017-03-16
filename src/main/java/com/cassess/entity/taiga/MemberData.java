package com.cassess.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="memberdata")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberData {

    @Id
    @Column(name="id")
    public int id;

    @Column(name="fullName")
    private String full_name;

    @Column(name="project")
    private String project_name;

    @Column(name="project_slug")
    private String project_slug;

    @Column(name="roleName")
    private String role_name;

    @Column(name="email")
    private String user_email;

    public MemberData(){

    }

    public MemberData(int id, String full_name, String project_name, String project_slug, String role_name, String user_email) {
        this.id = id;
        this.full_name = full_name;
        this.project_name = project_name;
        this.project_slug = project_slug;
        this.role_name = role_name;
        this.user_email = user_email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() { return full_name; }

    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getProject_name() { return project_name; }

    public void setProject_name(String project_name) { this.project_name = project_name; }

    public String getProject_slug() { return project_slug; }

    public void setProject_slug(String project_slug) { this.project_slug = project_slug; }

    public String getRole_name() { return role_name; }

    public void setRole_name(String role_name) { this.role_name = role_name; }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

}