package edu.asu.cassess.service.github.agReplacement;

import java.text.SimpleDateFormat;
import java.util.*;

class UserCommits {
    String userid;
    String name;
    Map<String, CommitDay> daily_activity = new TreeMap<>();
    AggregateActivity totalAct = new AggregateActivity("total_activity");
    AggregateActivity masterAct = new AggregateActivity("master_activity");
    ArrayList<Integer> inactivity = new ArrayList<>();


    public UserCommits(String userid, String name, Date start, Date end) {
        this.userid = userid;
        this.name = name;
        initDailyActivity(start,end);
    }

    private void initDailyActivity(Date start, Date end){
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar startC = new GregorianCalendar();
            Calendar endC = new GregorianCalendar();
            startC.setTime(start);
            endC.setTime(end);
            ArrayList<Date> dates = new ArrayList();
            while (startC.before(endC)){
                dates.add(startC.getTime());
                startC.add(Calendar.DATE,1);
            }
            dates.add(end);
            for(Date d:dates) {
                daily_activity.put(df.format(d), new CommitDay());
            }
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public String toString() {
        int streak = 0;
        //CommitDay cd;
        Collection<CommitDay>cds = daily_activity.values();
        for(CommitDay cd : cds){
            if(cd.commitList.size()==0)
                streak++;
            else {
                if(streak>1)
                    inactivity.add(streak);
                streak = 0;
            }
        }
        Integer[] streaks = inactivity.toArray(new Integer[inactivity.size()]);
        String ret = "{" +
                "\"userid\":\"" + userid + '\"' +
                ",\"name\":\"" + name + '\"' +
                ",\"daily_activity\":[";
        for(CommitDay d:daily_activity.values()){
            ret+=d.toString()+",";
        }
        ret = ret.substring(0, ret.length()-1);
        ret+="],";
        ret+=totalAct.toString()+","+masterAct.toString();
        ret+=",\"inactivity_streaks\":[";
        for(Integer i:streaks)
            ret+=i+",";
        ret=ret.substring(0,ret.length()-1);
        ret +="]}";
        return ret;
    }

    protected void addCommit(Commit c){
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            CommitDay cd;
            String date = df.format(df.parse(c.timestamp));
            cd = daily_activity.get(date);
            cd.addCommit(c);
            daily_activity.put(date,cd);
            if (c.branch.equals("master"))
                masterAct.addCommit(c);
            totalAct.addCommit(c);
        } catch (Exception e){e.printStackTrace();}
    }
}
