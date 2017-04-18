package edu.asu.cassess.model.github;

import edu.asu.cassess.dao.github.GitHubCommitDataDao;
import edu.asu.cassess.persist.entity.github.CommitData;

import java.sql.Date;

public class GitHubAnalytics {

    public static int calculateWeight(String email, Date date){
        GitHubCommitDataDao dao = new GitHubCommitDataDao();
        CommitData commitData = dao.getCommit(email, date);

        int linesOfCodeAdded = commitData.getLinesOfCodeAdded();
        int linesOfCodeDeleted = commitData.getLinesOfCodeDeleted();
        int totalCodeAleration = linesOfCodeAdded + (linesOfCodeDeleted / 2);

        int weight;

        if(linesOfCodeAdded < linesOfCodeDeleted){
            weight = 2;
        }else if(linesOfCodeAdded >= 250){
            weight = 5;
        }else if(linesOfCodeAdded >= 100 && linesOfCodeAdded < 250){
            weight = 3;
        }else if(linesOfCodeAdded < 100 && linesOfCodeAdded > 10){
            weight = 2;
        }else{
          weight = 0;
        }

        return weight;
    }
}
