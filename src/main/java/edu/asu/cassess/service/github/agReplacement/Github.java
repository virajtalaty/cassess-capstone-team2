package edu.asu.cassess.service.github.agReplacement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Github {
    private String owner;
    private String repo;
    private String token;
    private String[] members;
    QueryCaller caller;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public Github(String owner, String repo, String token, String[] members) {
        this.owner = owner;
        this.repo = repo;
        this.token = token;
        this.members = members;
        caller = new QueryCaller(token, owner, repo);
        System.out.println(owner+"\n"+repo);
        for(String m:members){
            System.out.println(m);
        }
    }
    public String getComitDataAll(String start,String end){
        String res = "[";
        for(String m:members){
            res+=getCommitData(m,m,start,end)+",";
        }
        res = res.substring(0,res.length()-1)+"]";
        return res;
    }

    public String getCommitData(String user, String name, String start, String end) {
        UserCommits userCommits=null;
        try {
            String jsonData = null;
            userCommits = new UserCommits(user, name, df.parse(start), df.parse(end));
            JSONArray commits;
            JSONArray branches;
            branches = new JSONArray(caller.apiCall("/branches?access_token=" + token));
            String sha;
            ArrayList<BranchRunner> threads = new ArrayList<>();
            BranchRunner b;
            System.out.println(branches.length());
            for (int j = 0; j < branches.length(); j++) {
                JSONObject branch = branches.getJSONObject(j);
                b = new BranchRunner(userCommits, branch, caller, user, start, end);
                if(branch.getString("name").equals("master")&& threads.size()>0)
                    threads.add(0,b);
                else
                    threads.add(b);
            }
            for(BranchRunner br:threads){
                br.runBranch();
            }
/*            int running;
            do {
                //TimeUnit.MILLISECONDS.sleep(500);
                running = 0;
                for (BranchRunner b1 : threads) {
                    if (!b1.getState().equals(Thread.State.TERMINATED))
                        running++;
                }
            } while (running > 0);
            System.out.println("Total Commits: " + userCommits.getTotalCommits());
            System.out.println("Api call count: " + caller.calls)*/;

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Calls: "+caller.calls);
        return userCommits.toString().replaceAll("\n", "");
    }
}

