package com.cassess.model.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusData {

    @Column(name="status")
    private String name;

    public StatusData() {
    }

    public String getName() { return name; }

    private void setName(String name) { this.name = name; }


}