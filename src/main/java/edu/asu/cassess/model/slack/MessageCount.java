package edu.asu.cassess.model.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCount {

    @Id
    @Column(name = "Total")
    public int Total;

    public MessageCount() {
    }

    public MessageCount(int Total) {
        this.Total = Total;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

}
