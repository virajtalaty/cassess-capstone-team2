package edu.asu.cassess.persist.entity.taiga;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class TaskTotalsID implements Serializable {

    @Column(name = "email")
    public String email;

    @Column(name = "retrievalDate")
    private String retrievalDate;

    public TaskTotalsID() {
    }

    public TaskTotalsID(String email) {
        this.email = email;
        Date date = new Date();
        this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getRetrievalDate() {
        return retrievalDate;
    }

    public void setRetrievalDate(String retrievalDate) {

        Date date = new Date();
        this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
