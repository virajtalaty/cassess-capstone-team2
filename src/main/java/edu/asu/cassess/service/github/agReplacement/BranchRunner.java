package edu.asu.cassess.service.github.agReplacement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

class BranchRunner extends Thread {
    UserCommits userCommits;
    JSONObject branch;
    QueryCaller caller;
    String user;
    String start;
    String end;
    String name;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    protected BranchRunner(UserCommits userCommits, JSONObject branch, QueryCaller caller, String user, String start, String end){
        this.userCommits = userCommits;
        this.branch = branch;
        this.caller = caller;
        this.user = user;
        this.start = start;
        this.end = end;
        name = branch.getString("name");
    }


    @Override
    public void run() {


        /*try {
            //join();
        }catch (Exception e){e.printStackTrace();}*/
    }
    public void runBranch(){
        try {

            String sha = branch.getJSONObject("commit").getString("sha");
            JSONArray commits = new JSONArray( caller.getCommits(user, start, end, sha));
            //UserCommits branch = new UserCommits("tim2162286","Tim Cuprak");
            for (int i = 0; i < commits.length(); i++) {
                JSONObject commit = commits.getJSONObject(i);
                String day = df.format(df.parse(commit.getJSONObject("commit").getJSONObject("author").getString("date")));
                String cSha = commit.getString("sha");
                CommitDay cDay = userCommits.daily_activity.get(day);
                if(cDay.commitList.containsKey(cSha)){
                    continue;
                }
                JSONObject commitDetail = new JSONObject(caller.getCommit(commit.getString("sha")));
                userCommits.addCommit(new Commit(commitDetail,name));
            }
            //System.out.println("BranchRunner "+name+" Complete");
        } catch (Exception e){
            System.out.println("Error in branch: "+name);
            e.printStackTrace();
        }
    }


}