package edu.asu.cassess.persist.entity.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="admins")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Admin {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String full_name;

    @Column(name = "course")
    private String course;

    public Admin(){

    }

    public Admin(String email, String full_name, String course) {
        this.email = email;
        this.full_name = full_name;
        this.course = course;
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

}
