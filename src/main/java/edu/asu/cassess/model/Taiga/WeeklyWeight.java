package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyWeight {

        @Id
        @Column(name="week")
        private String week;

        @Column(name="weekBeginning")
        private String weekBeginning;

        @Column(name="weekEnding")
        private String weekEnding;

        @Column(name="weight")
        private String weight;

        public WeeklyWeight(){}

        public WeeklyWeight(String week, String weekBeginning, String weekEnding, String weight) {
            this.week = week;
            this.weekBeginning = weekBeginning;
            this.weekEnding = weekEnding;
            this.weight = weight;
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

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

    }