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
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name="created_date")
    private String created_date;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(String created_date) {
        this.created_date = created_date;
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

}