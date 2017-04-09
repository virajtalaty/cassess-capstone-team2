package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyUpdateActivity {

    @Id
    @Column(name="week")
    private String week;

    @Column(name="weekBeginning")
    private String weekBeginning;

    @Column(name="weekEnding")
    private String weekEnding;

    @Column(name="DoneActivity")
    private int DoneActivity;

    @Column(name="InProgressActivity")
    private int InProgressActivity;

    @Column(name="ToTestActivity")
    private int ToTestActivity;

    public WeeklyUpdateActivity(){}

    public WeeklyUpdateActivity(String week, String weekBeginning, String weekEnding, int doneActivity, int inProgressActivity, int toTestActivity) {
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

    public int getDoneActivity() {
        return DoneActivity;
    }

    public void setDoneActivity(int doneActivity) {
        DoneActivity = doneActivity;
    }

    public int getInProgressActivity() {
        return InProgressActivity;
    }

    public void setInProgressActivity(int inProgressActivity) {
        InProgressActivity = inProgressActivity;
    }

    public int getToTestActivity() {
        return ToTestActivity;
    }

    public void setToTestActivity(int toTestActivity) {
        ToTestActivity = toTestActivity;
    }
}
