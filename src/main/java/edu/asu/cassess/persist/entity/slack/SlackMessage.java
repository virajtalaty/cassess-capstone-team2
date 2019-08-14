package edu.asu.cassess.persist.entity.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "slack_messages")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackMessage {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name="ts")
    private double ts;

    @Column(name="user")
    private String user;

    @Column(name = "text")
    String text;

    @Column(name = "type")
    String type;

    public SlackMessage() {

    }

    public SlackMessage(int id, double ts, String user, String text, String type) {
        this.id = id;
        this.ts = ts;
        this.user = user;
        this.text = text;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTs() {
        return ts;
    }

    public void setTs(double ts) {
        this.ts = ts;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
