package com.cassess.entity.github;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="commit_data")
public class CommitData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="commit_id")
    private int commitID;


    @Column(name="date")
    private Date date;

    @Column(name="username")
    private String username;

    @Column(name="lines_of_code_added")
    private int linesOfCodeAdded;

    @Column(name="lines_of_code_deleted")
    private int linesOfCodeDeleted;

    @Column(name="commits")
    private int commits;

    @Column(name="project_name")
    private String projectName;

    @Column(name="course_name")
    private String courseName;

    public CommitData() {
    }

    public CommitData(Date date, String username, int linesOfCodeAdded, int linesOfCodeDeleted, int commits, String projectName, String courseName) {
        this.date = date;
        this.username = username;
        this.linesOfCodeAdded = linesOfCodeAdded;
        this.linesOfCodeDeleted = linesOfCodeDeleted;
        this.commits = commits;
        this.projectName = projectName;
        this.courseName = courseName;
    }

    public int getCommitID() {
        return commitID;
    }

    public void setCommitID(int commitID) {
        this.commitID = commitID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLinesOfCodeAdded() {
        return linesOfCodeAdded;
    }

    public void setLinesOfCodeAdded(int linesOfCodeAdded) {
        this.linesOfCodeAdded = linesOfCodeAdded;
    }

    public int getLinesOfCodeDeleted() {
        return linesOfCodeDeleted;
    }

    public void setLinesOfCodeDeleted(int linesOfCodeDeleted) {
        this.linesOfCodeDeleted = linesOfCodeDeleted;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "CommitData{" +
//                "commitID='" + commitID + '\'' +
                ", date=" + date +
                ", username='" + username + '\'' +
                ", linesOfCodeAdded=" + linesOfCodeAdded +
                ", linesOfCodeDeleted=" + linesOfCodeDeleted +
                ", projectName='" + projectName + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}