package com.cassess.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="github_auth")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubAuth {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "token")
    private String token;

    public GitHubAuth() {

    }

    public GitHubAuth(int id, String token) {
        this.id = id;
        this.token = token;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
}