package com.cassess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="student")
public class Student {
    @Id
    @Column(name="asurite")
    private String asurite;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="team")
    private String team;

    @Column(name="gh_directory")
    private String ghDirectory;

    @Column(name="gh_login")
    private String ghLogin;

    public Student(){

    }

    public String getAsurite() {
        return asurite;
    }

    public void setAsurite(String asurite) {
        this.asurite = asurite;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getGhDirectory() {
        return ghDirectory;
    }

    public void setGhDirectory(String ghDirectory) {
        this.ghDirectory = ghDirectory;
    }

    public String getGhLogin() {
        return ghLogin;
    }

    public void setGhLogin(String ghLogin) {
        this.ghLogin = ghLogin;
    }
}