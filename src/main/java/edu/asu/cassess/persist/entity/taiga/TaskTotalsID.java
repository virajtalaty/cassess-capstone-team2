package edu.asu.cassess.persist.entity.taiga;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class TaskTotalsID implements Serializable{

    @Column(name = "id")
    public int id;

    @Column(name = "retrievalDate")
    private String retrievalDate;

    public TaskTotalsID() {
    }

    public TaskTotalsID(int id) {
        this.id = id;
        Date date = new Date();
        this.retrievalDate =  new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }


    public String getRetrievalDate() {
        return retrievalDate;
    }

    public void setRetrievalDate(String retrievalDate) {

        Date date = new Date();
        this.retrievalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
