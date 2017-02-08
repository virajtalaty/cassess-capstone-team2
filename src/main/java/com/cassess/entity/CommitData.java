package com.cassess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name="commit_data")
public class CommitData {
    @Id
    @Column(name="commit_id")
    private String commitID;

    @Column(name="date")
    private Date date;

    @Column(name="email")
    private String email;

    @Column(name="lines_of_code_added")
    private int linesOfCodeAdded;

    @Column(name="lines_of_code_deleted")
    private int linesOfCodeDeleted;

    @Column(name="project_name")
    private String projectName;

    @Column(name="course_name")
    private String courseName;

    public CommitData() {
    }

    public CommitData(String commitID, Date date, String email, int linesOfCodeAdded, int linesOfCodeDeleted, String projectName, String courseName) {
        this.commitID = commitID;
        this.date = date;
        this.email = email;
        this.linesOfCodeAdded = linesOfCodeAdded;
        this.linesOfCodeDeleted = linesOfCodeDeleted;
        this.projectName = projectName;
        this.courseName = courseName;
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                "commitID='" + commitID + '\'' +
                ", date=" + date +
                ", email='" + email + '\'' +
                ", linesOfCodeAdded=" + linesOfCodeAdded +
                ", linesOfCodeDeleted=" + linesOfCodeDeleted +
                ", projectName='" + projectName + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}