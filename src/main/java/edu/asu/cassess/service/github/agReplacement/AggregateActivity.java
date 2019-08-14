package edu.asu.cassess.service.github.agReplacement;

public class AggregateActivity {
    String name;
    int additions;
    int deletions;
    int total;
    int commits;
    public AggregateActivity(String name){
        this.name = name;
        additions = 0;
        deletions = 0;
        total = 0;
        commits = 0;
    }

    public void addCommit(Commit c){
        commits++;
        additions+=c.additions;
        deletions+=c.deletions;
        total+=c.total;
    }

    @Override
    public String toString() {
        return "\""+name+"\":{" +
                "\"commits\":" + commits  +
                ",\"additions\":" + additions +
                ",\"deletions\":" + deletions +
                ",\"total\":" + total +
                '}';
    }
}
