package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyAverages {

    @Id
    @Column(name="week")
    private String week;

    @Column(name="weekBeginning")
    private String weekBeginning;

    @Column(name="weekEnding")
    private String weekEnding;

    @Column(name="DoneAverage")
    private int DoneAverage;

    @Column(name="InProgressAverage")
    private int InProgressAverage;

    @Column(name="ToTestAverage")
    private int ToTestAverage;

    public WeeklyAverages(){}


    public WeeklyAverages(String week, String weekBeginning, String weekEnding, int doneAverage, int inProgressAverage, int toTestAverage) {
        this.week = week;
        this.weekBeginning = weekBeginning;
        this.weekEnding = weekEnding;
        DoneAverage = doneAverage;
        InProgressAverage = inProgressAverage;
        ToTestAverage = toTestAverage;
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

    public int getDoneAverage() {
        return DoneAverage;
    }

    public void setDoneAverage(int doneAverage) {
        DoneAverage = doneAverage;
    }

    public int getInProgressAverage() {
        return InProgressAverage;
    }

    public void setInProgressAverage(int inProgressAverage) {
        InProgressAverage = inProgressAverage;
    }

    public int getToTestAverage() {
        return ToTestAverage;
    }

    public void setToTestAverage(int toTestAverage) {
        ToTestAverage = toTestAverage;
    }


}
