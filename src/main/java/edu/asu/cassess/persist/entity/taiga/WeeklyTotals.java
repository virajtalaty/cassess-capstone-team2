package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Subselect("SELECT\n" +
        "    tasktotals.id,\n" +
        "    tasktotals.fullName,\n" +
        "    (\n" +
        "        SELECT\n" +
        "            MIN(tasktotalsmin.retrievalDate)\n" +
        "        FROM tasktotals AS tasktotalsmin\n" +
        "        WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate\n" +
        "            AND tasktotals.id = tasktotalsmin.id\n" +
        "    ) AS WeekEnding,\n" +
        "    (\n" +
        "        SELECT\n" +
        "            GREATEST(MIN(tasktotalsmin.tasksClosed) - tasktotals.tasksClosed, 0)\n" +
        "        FROM tasktotals AS tasktotalsmin\n" +
        "        WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate\n" +
        "            AND tasktotals.id = tasktotalsmin.id\n" +
        "    ) AS ClosedTasks,\n" +
        "    (\n" +
        "        SELECT\n" +
        "            GREATEST(MIN(tasktotalsmin.tasksOpen) - tasktotals.tasksOpen, 0)\n" +
        "        FROM tasktotals AS tasktotalsmin\n" +
        "        WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate\n" +
        "            AND tasktotals.id = tasktotalsmin.id\n" +
        "    ) AS OpenTasks,\n" +
        "    (\n" +
        "        SELECT\n" +
        "            GREATEST(MIN(tasktotalsmin.tasksNew) - tasktotals.tasksNew, 0)\n" +
        "        FROM tasktotals AS tasktotalsmin\n" +
        "        WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate\n" +
        "            AND tasktotals.id = tasktotalsmin.id\n" +
        "    ) AS NewTasks,\n" +
        "    (\n" +
        "        SELECT\n" +
        "            GREATEST(MIN(tasktotalsmin.tasksInProgress) - tasktotals.tasksInProgress, 0)\n" +
        "        FROM tasktotals AS tasktotalsmin\n" +
        "        WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate\n" +
        "            AND tasktotals.id = tasktotalsmin.id\n" +
        "    ) AS InProgressTasks,\n" +
        "    (\n" +
        "        SELECT\n" +
        "            GREATEST(MIN(tasktotalsmin.tasksReadyForTest) - tasktotals.tasksReadyForTest, 0)\n" +
        "        FROM tasktotals AS tasktotalsmin\n" +
        "        WHERE tasktotalsmin.retrievalDate > tasktotals.retrievalDate\n" +
        "            AND tasktotals.id = tasktotalsmin.id\n" +
        "    ) ReadyForTestTasks\n" +
        "FROM tasktotals WHERE fullName = = ?1")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyTotals {

    @Id
    @Column(name="WeekEnding")
    private String WeekEnding;

    @Column(name="fullName")
    private String fullName;

    @Column(name="ClosedTasks")
    private int ClosedTasks;

    @Column(name="NewTasks")
    private int NewTasks;

    @Column(name="InProgressTasks")
    private int InProgressTasks;

        @Column(name="ReadyForTestTasks")
    private int ReadyForTestTasks;

    @Column(name="OpenTasks")
    private int OpenTasks;



    public WeeklyTotals(){

    }

    public WeeklyTotals(String WeekEnding, String fullName, int ClosedTasks, int NewTasks, int InProgressTasks, int ReadyForTestTasks, int OpenTasks) {
        this.WeekEnding = WeekEnding;
        this.fullName = fullName;
        this.ClosedTasks = ClosedTasks;
        this.NewTasks = NewTasks;
        this.InProgressTasks = InProgressTasks;
        this.ReadyForTestTasks = ReadyForTestTasks;
        this.OpenTasks = OpenTasks;
    }

    public String getWeekEnding() {
        return WeekEnding;
    }

    public void setWeekEnding(String WeekEnding) {

        Date date = new Date();
        this.WeekEnding = new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getClosedTasks() {
        return ClosedTasks;
    }

    public void setClosedTasks(int ClosedTasks) {
        ClosedTasks = ClosedTasks;
    }

    public int getNewTasks() {
        return NewTasks;
    }

    public void setNewTasks(int NewTasks) {
        NewTasks = NewTasks;
    }

    public int getInProgressTasks() {
        return InProgressTasks;
    }

    public void setInProgressTasks(int InProgressTasks) {
        InProgressTasks = InProgressTasks;
    }

    public int getReadyForTestTasks() {
        return ReadyForTestTasks;
    }

    public void setReadyForTestTasks(int ReadyForTestTasks) {
        ReadyForTestTasks = ReadyForTestTasks;
    }

    public int getOpenTasks() {
        return OpenTasks;
    }

    public void setOpenTasks(int OpenTasks) {
        OpenTasks = OpenTasks;
    }


}

