package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyTaskTotals {

    @Id
    @Column(name = "Date")
    private String Date;

    @Column(name = "InProgress")
    private int InProgress;

    @Column(name = "ToTest")
    private int ToTest;

    @Column(name = "Done")
    private int Done;

    public DailyTaskTotals() {
    }

    public DailyTaskTotals(String date, int inProgress, int toTest, int done) {
        Date = date;
        InProgress = inProgress;
        ToTest = toTest;
        Done = done;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getInProgress() {
        return InProgress;
    }

    public void setInProgress(int inProgress) {
        InProgress = inProgress;
    }

    public int getToTest() {
        return ToTest;
    }

    public void setToTest(int toTest) {
        ToTest = toTest;
    }

    public int getDone() {
        return Done;
    }

    public void setDone(int done) {
        Done = done;
    }
}
