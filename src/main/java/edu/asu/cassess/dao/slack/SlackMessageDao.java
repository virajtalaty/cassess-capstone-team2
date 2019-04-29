package edu.asu.cassess.dao.slack;

import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.persist.entity.slack.SlackMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Component
public class SlackMessageDao implements ISlackMessageDao {

    protected EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {return entityManager;}



    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public double getTimeOfLastMessage(String chanel) {
        double dateStamp;

        try {
            dateStamp = (double) entityManager
                    .createQuery("SELECT MAX(m.ts) FROM SlackMessage m where m.channel_id = ?1")
                    .setParameter(1,chanel)
                    .getSingleResult();
            System.out.println(dateStamp);
        }catch (Exception e){
            System.out.println();
            dateStamp = 0;
        }
        return dateStamp;
    }

    @Override
    @Transactional
    public boolean getMessageExists(double ts) {
        boolean exists;
        try{
            List list = entityManager.createQuery("select m from SlackMessage m where m.ts = ?1")
                    .setParameter(1, ts)
                    .getResultList();
            return list.size()>0;
        }catch (Exception e){return false;}
    }

    @Override
    @Transactional
    public List<SlackMessage> getStudentMessages(String user, long start, long end) {
        List<SlackMessage> messages;
        try {
            messages = (List<SlackMessage>) entityManager
                    .createQuery("SELECT m FROM SlackMessage m WHERE m.user = ?1 and m.ts<=?2 and m.ts>?3")
                    .setParameter(1, user)
                    .setParameter(2, start)
                    .setParameter(3, end)
                    .getResultList();
        }catch (Exception e){
            messages = null;
        }
        return messages;
    }

    @Override
    @Transactional
    public int getStudentMessageCount(String user, long start, long end) {
        return (int)entityManager.createQuery("SELECT COUNT(m.id) FROM SlackMessage m where m.user = ?1 and m.ts<=?2 and m.ts>?3")
                .setParameter(1, user)
                .setParameter(2, start)
                .setParameter(3, end)
                .getSingleResult();
    }

    @Override
    @Transactional
    public List<DailyMessageTotals> getStudentDailyTotals(String user, long start, long end){
        List<DailyMessageTotals> totals = new ArrayList<DailyMessageTotals>();

        long[] day = getDay(start);
        while (day[0] <= end){
            DailyMessageTotals total = new DailyMessageTotals();
            int cnt = getStudentMessageCount(user, day[0], day[1]);
            total.setDate(new Date(day[0]).toString());
            total.setTotal(cnt);
            totals.add(total);
            day = getDay(day[1]+1);
        }
        return totals;
    }

    @Override
    @Transactional
    public List<SlackMessage> getTeamMessages(String team, long start, long end) {
        List<SlackMessage> messages;
        try {
            messages = (List<SlackMessage>) entityManager
                    .createQuery("SELECT m FROM SlackMessage m WHERE m.team = ?1 and m.ts<=?2 and m.ts>?3")
                    .setParameter(1, team)
                    .setParameter(2, start)
                    .setParameter(3, end)
                    .getResultList();
        }catch (Exception e){
            messages = null;
        }
        return messages;
    }

    @Override
    @Transactional
    public int getTeamMessageCount(String team, long start, long end) {
        return (int)entityManager.createQuery("SELECT COUNT(m.id) FROM SlackMessage m where m.team = ?1 and m.ts<=?2 and m.ts>?3")
                .setParameter(1, team)
                .setParameter(2, (double)start)
                .setParameter(3, (double)end)
                .getSingleResult();
    }

    @Override
    public List<DailyMessageTotals> getTeamDailyTotals(String team, long start, long end) {
        List<DailyMessageTotals> totals = new ArrayList<DailyMessageTotals>();

        long[] day = getDay(start);
        while (day[0] <= end){
            DailyMessageTotals total = new DailyMessageTotals();
            int cnt = getTeamMessageCount(team, day[0], day[1]);
            total.setDate(new Date(day[0]).toString());
            total.setTotal(cnt);
            totals.add(total);
            day = getDay(day[1]+1);
        }
        return totals;
    }

    private long[] getDay(long start){
        Calendar dayStart = new GregorianCalendar();
        Calendar dayEnd = new GregorianCalendar();
        dayStart.setTimeInMillis(start);
        dayStart.set(Calendar.HOUR_OF_DAY,0);
        dayStart.set(Calendar.MINUTE,0);
        dayStart.set(Calendar.SECOND,0);
        dayStart.set(Calendar.MILLISECOND,0);
        dayEnd.setTimeInMillis(start);
        dayEnd.set(Calendar.HOUR_OF_DAY,23);
        dayEnd.set(Calendar.MINUTE,59);
        dayEnd.set(Calendar.SECOND,59);
        dayEnd.set(Calendar.MILLISECOND,999);
        return new long[]{dayStart.getTimeInMillis(),dayEnd.getTimeInMillis()};
    }
}
