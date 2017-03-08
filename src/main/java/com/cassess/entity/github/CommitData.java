package com.cassess.entity.github;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="commit_data")
public class CommitData{
    @EmbeddedId
    private CommitDataPK commitDataPK;

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
        this.commitDataPK = new CommitDataPK(date, username);
        this.linesOfCodeAdded = linesOfCodeAdded;
        this.linesOfCodeDeleted = linesOfCodeDeleted;
        this.commits = commits;
        this.projectName = projectName;
        this.courseName = courseName;
    }

    public CommitDataPK getCommitDataPK() {
        return commitDataPK;
    }

    public void setCommitDataPK(CommitDataPK commitDataPK) {
        this.commitDataPK = commitDataPK;
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

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
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
                "commitDataPK=" + commitDataPK +
                ", linesOfCodeAdded=" + linesOfCodeAdded +
                ", linesOfCodeDeleted=" + linesOfCodeDeleted +
                ", commits=" + commits +
                ", projectName='" + projectName + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}