package com.cassess.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Subselect;

/**
 * Created by Thomas on 2/11/2017.
 */
@Entity
@Subselect("SELECT * FROM taskdata")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCount {

    @Id
    @Column(name="Total")
    public int Total;

    public TaskCount(){
    }

    public TaskCount(int Total){
        this.Total = Total;
    }

    public int getTotal() { return Total; }

    public void setTotal(int Total) { this.Total = Total; }

}
