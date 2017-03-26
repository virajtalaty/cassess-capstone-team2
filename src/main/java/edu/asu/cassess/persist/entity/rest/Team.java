package edu.asu.cassess.persist.entity.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="teams")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

    @Id
    @Column(name="team_name")
    private String team_name;

    @Column(name="course")
    private String course;

    @Column(name="slack_team_id")
    private String slack_team_id;

    @Column(name="github_repo_id")
    private String github_repo_id;

    @Column(name="taiga_project_slug")
    private String taiga_project_slug;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team_name", fetch = FetchType.EAGER)
    private List<Student> students;

    public Team(String team_name, String course, String slack_team_id, String github_repo_id, String taiga_project_slug, List<Student> students) {
        this.team_name = team_name;
        this.course = course;
        this.slack_team_id = slack_team_id;
        this.github_repo_id = github_repo_id;
        this.taiga_project_slug = taiga_project_slug;
        this.students = students;
    }

    public Team(){

    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSlack_team_id() {
        return slack_team_id;
    }

    public void setSlack_team_id(String slack_team_id) {
        this.slack_team_id = slack_team_id;
    }

    public String getGithub_repo_id() {
        return github_repo_id;
    }

    public void setGithub_repo_id(String github_repo_id) {
        this.github_repo_id = github_repo_id;
    }

    public String getTaiga_project_slug() {
        return taiga_project_slug;
    }

    public void setTaiga_project_slug(String taiga_project_slug) {
        this.taiga_project_slug = taiga_project_slug;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        for(Student student:students){
            student.setTeam_name(team_name);
        }
        this.students = students;
    }
}
