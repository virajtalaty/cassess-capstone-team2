package com.cassess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name="all_commits")
public class AllCommits {
    @Id
    @Column(name="commit_id")
    private String commitId;

    @Column(name="email")
    private String email;

    @Column(name="date")
    private Date date;

    @Column(name="project_name")
    private String projectName;

    @Column(name="course_name")
    private String courseName;

    public AllCommits() {
    }

    public AllCommits(String commitId, String email, Date date, String projectName, String courseName) {
        this.commitId = commitId;
        this.email = email;
        this.date = date;
        this.projectName = projectName;
        this.courseName = courseName;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        return "AllCommits{" +
                "commitId='" + commitId + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", projectName='" + projectName + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
