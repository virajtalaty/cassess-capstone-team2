package com.cassess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="auth_token")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthToken {

    @Id
    @Column(name = "tool")
    private String tool;

    @Column(name = "token")
    private String token;

    public AuthToken() {

    }

    public AuthToken(String tool, String token) {
        this.tool = tool;
        this.token = token;
    }

    /**
     * @return the tool
     */
    public String getTool() {
        return tool;
    }

    /**
     * @param tool the tool to set
     */
    public void setTool(String tool) {
        this.tool = tool;
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