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

    @Column(name="team_name")
    private String team_name;

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

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

}