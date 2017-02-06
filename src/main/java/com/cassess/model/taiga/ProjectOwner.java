package com.cassess.model.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectOwner {

    @Column(name="member_id")
    private Long id;
    @Column(name="full_name")
    private String full_name_display;
    @Column(name="username")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFull_name_display() {
        return full_name_display;
    }

    public void setFull_name_display(String full_name_display) {
        this.full_name_display = full_name_display;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
