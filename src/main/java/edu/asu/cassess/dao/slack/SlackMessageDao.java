package edu.asu.cassess.dao.slack;

import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.persist.entity.slack.SlackMessageTotals;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class SlackMessageDao implements ISlackMessageDao {

    protected EntityManager entityManager;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public EntityManager getEntityManager() {return entityManager;}



    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public long getTimeOfLastMessage(String chanel) {
        long dateStamp;
        try {
            String date = (String) entityManager
                    .createQuery("SELECT MAX(m.retrievalDate) FROM SlackMessageTotals m where m.channel_id = ?1")
                    .setParameter(1,chanel)
                    .getSingleResult();
            dateStamp = df.parse(date).getTime();
        }catch (Exception e){
            dateStamp = 0;
        }
        return dateStamp;
    }

    @Override
    @Transactional
    public List<SlackMessageTotals> getStudentMessagesInf(String course, String team, String email, String day) {
        List<SlackMessageTotals> messageTotals;
        try {
            messageTotals = (List<SlackMessageTotals>) entityManager
                    .createQuery("SELECT m FROM SlackMessageTotals m WHERE m.course = ?1 and m.team = ?2 and m.compositeId.email = ?3 and m.compositeId.retrievalDate=?4")
                    .setParameter(1,course)
                    .setParameter(2,team)
                    .setParameter(3,email)
                    .setParameter(4, day)
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
            messageTotals = null;
        }
        return messageTotals;
    }

    @Override
    @Transactional
    public List<DailyMessageTotals> getStudentDailyTotals(String course, String team, String email, long start, long end){
        List<DailyMessageTotals> totals = new ArrayList<DailyMessageTotals>();

        long day = start;
        while (day <= end){
            String dayString = df.format(new Date(day));
            DailyMessageTotals total = new DailyMessageTotals();
            List<SlackMessageTotals> messageTotals = getStudentMessagesInf(course, team, email, dayString);
            int cnt = 0;
            if(messageTotals.size()>0) {
                for (SlackMessageTotals messageTotal : messageTotals)
                    cnt += messageTotal.getMessage_count();
            }
            total.setDate(dayString);
            total.setTotal(cnt);
            totals.add(total);
            day += TimeUnit.DAYS.toMillis(1);
        }
        return totals;
    }

    @Override
    @Transactional
    public List<SlackMessageTotals> getTeamMessagesInf(String course, String team, String day) {
        List<SlackMessageTotals> messages;
        System.out.println(day);
        try {
            messages = (List<SlackMessageTotals>) entityManager
                    .createQuery("SELECT m FROM SlackMessageTotals m WHERE m.course = ?1 and m.team = ?2 and m.compositeId.retrievalDate=?3")
                    .setParameter(1, course)
                    .setParameter(2, team)
                    .setParameter(3, day)
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
            messages = null;
        }
        return messages;
    }

    @Override
    public List<DailyMessageTotals> getTeamDailyTotals(String course, String team, long start, long end) {
        List<DailyMessageTotals> totals = new ArrayList<DailyMessageTotals>();
        System.out.println("Start: "+start);
        long day = start;
        while (day <= end){
            String dayString = df.format(new Date(day));
            DailyMessageTotals total = new DailyMessageTotals();
            List<SlackMessageTotals> messageTotals = getTeamMessagesInf(course,team,dayString);
            int cnt = 0;
            if(messageTotals.size()>0) {
                for (SlackMessageTotals messageTotal : messageTotals)
                    cnt += messageTotal.getMessage_count();
            }
            total.setDate(dayString);
            total.setTotal(cnt);
            totals.add(total);
            day += TimeUnit.DAYS.toMillis(1);
        }
        return totals;
    }

    @Override
    @Transactional
    public List<SlackMessageTotals> getCourseMessagesInf(String course, String day) {
        List<SlackMessageTotals> messageTotals;
        try {
            messageTotals = (List<SlackMessageTotals>) entityManager
                    .createQuery("SELECT m FROM SlackMessageTotals m WHERE m.course = ?1 and m.compositeId.retrievalDate=?2")
                    .setParameter(1, course)
                    .setParameter(2, day)
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
            messageTotals = null;
        }
        return messageTotals;
    }

    @Override
    public List<DailyMessageTotals> getCourseDailyTotals(String course, long start, long end) {
        List<DailyMessageTotals> totals = new ArrayList<DailyMessageTotals>();

        long day = start;
        while (day <= end){
            String dayString = df.format(new Date(day));
            DailyMessageTotals total = new DailyMessageTotals();
            List<SlackMessageTotals> messageTotals = getCourseMessagesInf(course,dayString);
            int cnt = 0;
            if(messageTotals.size()>0) {
                for (SlackMessageTotals messageTotal : messageTotals)
                    cnt += messageTotal.getMessage_count();
            }
            total.setDate(df.format(new Date(day)));
            total.setTotal(cnt);
            totals.add(total);
            day += TimeUnit.DAYS.toMillis(1);
        }
        return totals;
    }

}
