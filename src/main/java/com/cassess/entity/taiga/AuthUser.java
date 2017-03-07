package com.cassess.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "taiga_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUser {
    
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "auth_token")
    private String auth_token;
    @Column(name = "email")
    private String email;
    @Column(name = "full_name")
    private String full_name;
    @Column(name = "full_name_display")
    private String full_name_display;
    @Column(name = "gravatar_id")
    private String gravatar_id;
    @Column(name = "is_active")
    private boolean is_active;


    public AuthUser() {

    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
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

    public String getFull_name_display() {
        return full_name_display;
    }

    public void setFull_name_display(String full_name_display) {
        this.full_name_display = full_name_display;
    }

    public String getGravatar_id() {
        return gravatar_id;
    }

    public void setGravatar_id(String gravatar_id) {
        this.gravatar_id = gravatar_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
