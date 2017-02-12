package com.cassess.model.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thomas on 2/10/2017.
 */

    @Entity
    @Table(name="tasktotals")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TaskTotals {

        @Id
        @Column(name="id")
        public int id;

        @Column(name="fullName")
        private String full_name;

        @Column(name="project")
        private String project_name;

        @Column(name="roleName")
        private String role_name;

        @Column(name="tasksClosed")
        private int tasks_closed;

        @Column(name="tasksNew")
        private int tasks_new;

        @Column(name="tasksInProgress")
        private int tasks_in_progress;

        @Column(name="tasksReadyForTest")
        private int tasks_ready_for_test;

        @Column(name="tasksOpen")
        private int tasks_open;

        @Column(name="retrievalDate")
        private String retrievalDate;

        public TaskTotals(){

        }

        public TaskTotals(int id, String full_name, String project_name, String role_name, int tasks_closed, int tasks_new, int tasks_in_progress, int tasks_ready_for_test, int tasks_open){
            this.id = id;
            this.full_name = full_name;
            this.project_name = project_name;
            this.role_name = role_name;
            this.tasks_closed = tasks_closed;
            this.tasks_new = tasks_new;
            this.tasks_in_progress = tasks_in_progress;
            this.tasks_ready_for_test = tasks_ready_for_test;
            this.tasks_open = tasks_open;
            Date date = new Date();
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
            this.retrievalDate = dateString;
        }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFull_name() { return full_name; }

    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getProject_name() { return project_name; }

    public void setProject_name(String project_name) { this.project_name = project_name; }

    public String getRole_name() { return role_name; }

    public void setRole_name(String role_name) { this.role_name = role_name; }

    public int getTasks_closed() { return tasks_closed; }

    public void setTasks_closed(int tasks_closed) { this.tasks_closed = tasks_closed; }

    public int getTasks_new() { return tasks_new; }

    public void setTasks_new(int tasks_new) { this.tasks_new = tasks_new; }

    public int getTasks_in_progress() { return tasks_in_progress; }

    public void setTasks_in_progress(int tasks_in_progress) { this.tasks_in_progress = tasks_in_progress; }

    public int getTasks_ready_for_test() { return tasks_ready_for_test; }

    public void setTasks_ready_for_test(int tasks_ready_for_test) { this.tasks_ready_for_test = tasks_ready_for_test; }

    public int getTasks_open() { return tasks_open; }

    public void setTasks_open(int tasks_open) { this.tasks_open = tasks_open; }

    public String getRetrievalDate() {
        return retrievalDate;
    }

    public void setRetrievalDate(String retrievalDate) {

        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);

        this.retrievalDate = dateString;
    }



}
