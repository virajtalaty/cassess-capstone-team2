package com.cassess.model.github;

import com.cassess.entity.CommitData;
import com.cassess.model.GatherData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

@Service
@Transactional
public class GatherGitHubData implements GatherData {
    @Autowired
    private GitHubCommitDataDao dao;
    private String urlPath;

    public GatherGitHubData() {
        this.urlPath = "tjjohn1/cassess-capstone-team2";
    }

    /**
     * Implements the GatherData interface method
     */
    @Override
    public void fetchData(){
        getAllCommits();
    }

    /**
     * Gathers all commits to the repository, gathers their Sha IDs, and passes them to the getCommit method
     */
    private void getAllCommits(){
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject("https://api.github.com/repos/"+ urlPath + "/commits", String.class);
        ArrayList<GitHubSha> gitHubAllCommits = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            gitHubAllCommits = mapper.readValue(json, new TypeReference<ArrayList<GitHubSha>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }


        getCommit(gitHubAllCommits);
    }

    /**
     * Takes the Sha IDs and gathers the relevant data for each commit
     * @param gitHubCommits     Arraylist full of Sha Keys
     */
    private void getCommit(ArrayList<GitHubSha> gitHubCommits){
        RestTemplate restTemplate = new RestTemplate();

        for (GitHubSha sha: gitHubCommits) {
            GitHubCommitData json = restTemplate.getForObject("https://api.github.com/repos/"+ urlPath + "/commits/" + sha.getSha(), GitHubCommitData.class);

            String commitId = sha.getSha();
            Date date = json.getCommit().getAuthor().getDate();
            String email = json.getCommit().getAuthor().getEmail();
            int linesOfCodeAdded = json.getStats().getAdditions();
            int linesOfCodeDeleted = json.getStats().getDeletions();

            CommitData commitData = new CommitData(commitId, date, email,linesOfCodeAdded,linesOfCodeDeleted);

            dao.save(commitData);
        }

    }
}