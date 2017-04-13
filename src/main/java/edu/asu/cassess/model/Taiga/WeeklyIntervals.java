package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyIntervals {

    @Id
    @Column(name="week")
    private String week;

    @Column(name="weekBeginning")
    private String weekBeginning;

    @Column(name="weekEnding")
    private String weekEnding;

    public WeeklyIntervals(){}

    public WeeklyIntervals(String week, String weekBeginning, String weekEnding) {
        this.week = week;
        this.weekBeginning = weekBeginning;
        this.weekEnding = weekEnding;
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

}
