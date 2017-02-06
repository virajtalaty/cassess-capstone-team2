package com.cassess.model.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "projects")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    @Id
    @Column(name = "created_date")
    private String created_date;
    @Column(name = "creation_template")
    private Long creation_template;
    @Column(name = "default_epic_status")
    private Long default_epic_status;
    @Column(name = "default_issue_status")
    private Long default_issue_status;
    @Column(name = "default_issue_type")
    private Long default_issue_type;
    @Column(name = "default_points")
    private Long default_points;
    @Column(name = "default_priority")
    private Long default_priority;
    @Column(name = "default_severity")
    private Long default_severity;
    @Column(name = "default_task_status")
    private Long default_task_status;
    @Column(name = "default_us_status")
    private Long default_us_status;
    @Column(name = "i_am_admin")
    private boolean i_am_admin;
    @Column(name = "i_am_member")
    private boolean i_am_member;
    @Column(name = "i_am_owner")
    private boolean i_am_owner;
    @Column(name = "id")
    private Long id;
    @Column(name = "modified_date")
    private String modified_date;
    @Column(name = "name")
    private String name;
    @Column(name = "slug")
    private String slug;
    @Column(name = "total_activity")
    private Long total_activity;
    @Column(name = "total_milestones")
    private Long total_milestones;

    public ProjectOwner getOwner() {
        return owner;
    }

    public void setOwner(ProjectOwner owner) {
        this.owner = owner;
    }

    private ProjectOwner owner;

    public Project() {
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public Long getCreation_template() {
        return creation_template;
    }

    public void setCreation_template(Long creation_template) {
        this.creation_template = creation_template;
    }

    public Long getDefault_epic_status() {
        return default_epic_status;
    }

    public void setDefault_epic_status(Long default_epic_status) {
        this.default_epic_status = default_epic_status;
    }

    public Long getDefault_issue_status() {
        return default_issue_status;
    }

    public void setDefault_issue_status(Long default_issue_status) {
        this.default_issue_status = default_issue_status;
    }

    public Long getDefault_issue_type() {
        return default_issue_type;
    }

    public void setDefault_issue_type(Long default_issue_type) {
        this.default_issue_type = default_issue_type;
    }

    public Long getDefault_points() {
        return default_points;
    }

    public void setDefault_points(Long default_points) {
        this.default_points = default_points;
    }

    public Long getDefault_priority() {
        return default_priority;
    }

    public void setDefault_priority(Long default_priority) {
        this.default_priority = default_priority;
    }

    public Long getDefault_severity() {
        return default_severity;
    }

    public void setDefault_severity(Long default_severity) {
        this.default_severity = default_severity;
    }

    public Long getDefault_task_status() {
        return default_task_status;
    }

    public void setDefault_task_status(Long default_task_status) {
        this.default_task_status = default_task_status;
    }

    public Long getDefault_us_status() {
        return default_us_status;
    }

    public void setDefault_us_status(Long default_us_status) {
        this.default_us_status = default_us_status;
    }

    public boolean isI_am_admin() {
        return i_am_admin;
    }

    public void setI_am_admin(boolean i_am_admin) {
        this.i_am_admin = i_am_admin;
    }

    public boolean isI_am_member() {
        return i_am_member;
    }

    public void setI_am_member(boolean i_am_member) {
        this.i_am_member = i_am_member;
    }

    public boolean isI_am_owner() {
        return i_am_owner;
    }

    public void setI_am_owner(boolean i_am_owner) {
        this.i_am_owner = i_am_owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getTotal_activity() {
        return total_activity;
    }

    public void setTotal_activity(Long total_activity) {
        this.total_activity = total_activity;
    }

    public Long getTotal_milestones() {
        return total_milestones;
    }

    public void setTotal_milestones(Long total_milestones) {
        this.total_milestones = total_milestones;
    }
}