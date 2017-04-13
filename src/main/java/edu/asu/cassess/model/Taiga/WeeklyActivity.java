package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyActivity {

    @Id
    @Column(name="week")
    private String week;

    @Column(name="weekBeginning")
    private String weekBeginning;

    @Column(name="weekEnding")
    private String weekEnding;

    @Column(name="DoneActivity")
    private String DoneActivity;

    @Column(name="InProgressActivity")
    private String InProgressActivity;

    @Column(name="ToTestActivity")
    private String ToTestActivity;

    public WeeklyActivity(){}


    public WeeklyActivity(String week, String weekBeginning, String weekEnding, String doneActivity, String inProgressActivity, String toTestActivity) {
        this.week = week;
        this.weekBeginning = weekBeginning;
        this.weekEnding = weekEnding;
        this.DoneActivity = doneActivity;
        this.InProgressActivity = inProgressActivity;
        this.ToTestActivity = toTestActivity;
    }



    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeekBeginning() {
        return weekBeginning;
    }

    public void setWeekBeginning(String weekBeginning) {
        this.weekBeginning = weekBeginning;
    }

    public String getWeekEnding() {
        return weekEnding;
    }

    public void setWeekEnding(String weekEnding) {
        this.weekEnding = weekEnding;
    }

    public String getDoneActivity() {
        return DoneActivity;
    }

    public void setDoneActivity(String doneActivity) {
        DoneActivity = doneActivity;
    }

    public String getInProgressActivity() {
        return InProgressActivity;
    }

    public void setInProgressActivity(String inProgressActivity) {
        InProgressActivity = inProgressActivity;
    }

    public String getToTestActivity() {
        return ToTestActivity;
    }

    public void setToTestActivity(String toTestActivity) {
        ToTestActivity = toTestActivity;
    }


}
