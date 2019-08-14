package edu.asu.cassess.service.github.agReplacement;

import java.util.HashMap;

class CommitDay{
    protected int commits;
    protected int additions;
    protected int deletions;
    protected int total;
    protected HashMap<String,Commit> commitList;

    protected CommitDay() {
        commits = 0;
        additions = 0;
        deletions = 0;
        total = 0;
        commitList = new HashMap<>();
    }

    protected void addCommit(Commit commit){
        if(!commitList.containsKey(commit.sha)) {
            commits++;
            additions += commit.additions;
            deletions += commit.deletions;
            total += commit.total;
            commitList.put(commit.sha,commit);
        }
    }

    @Override
    public String toString() {
        Commit[] details = commitList.values().toArray(new Commit[commitList.size()]);
        return "{" +
                "\"commits\":" + commits +
                ",\"additions\":" + additions +
                ",\"deletions\":" + deletions +
                ",\"total\":" + total +
                ",\"commit_details\":" + commitList.values().toString() +
                '}';
    }

    public int getCommits(){
        return commitList.size();
    }
}
