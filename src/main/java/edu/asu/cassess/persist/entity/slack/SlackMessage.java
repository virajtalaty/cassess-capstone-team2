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

    @Column(name="type")
    private String type;

    @Column(name="text")
    private String text;

    @Column(name="channel_id")
    private String channel_id;

    @Column(name = "course")
    private String course;

    @Column(name = "team")
    private String team;

    public SlackMessage() {

    }

    public SlackMessage(int id, double ts, String user, String type, String text, String channel_id, String course, String team) {
        this.id = id;
        this.ts = ts;
        this.user = user;
        this.type = type;
        this.text = text;
        this.channel_id = channel_id;
        this.course = course;
        this.team = team;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        int maxLength = (text.length() < 255)?text.length():255;
        text = text.substring(0, maxLength);
        this.text = text;
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

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
