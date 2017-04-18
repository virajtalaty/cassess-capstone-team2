package edu.asu.cassess.model.github;

public class GitHubAnalytics {
    public static int calculateWeight(int linesOfCodeAdded, int linesOfCodeDeleted){
        int totalCodeAleration = linesOfCodeAdded + (linesOfCodeDeleted / 2);

        int weight;
        if(linesOfCodeAdded < linesOfCodeDeleted){
            weight = 2;
        }else if(totalCodeAleration >= 250){
            weight = 5;
        }else if(totalCodeAleration >= 100 && totalCodeAleration < 250){
            weight = 3;
        }else if(totalCodeAleration < 100 && totalCodeAleration > 10){
            weight = 2;
        }else{
          weight = 0;
        }

        return weight;
    }
}
