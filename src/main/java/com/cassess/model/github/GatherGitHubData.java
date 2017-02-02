package com.cassess.model.github;

import com.cassess.entity.CommitData;
import com.cassess.model.GatherData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

@Service
@Transactional
public class GatherGitHubData implements GatherData {

    @Autowired
    private GitHubCommitDataDao dao;
    private RestTemplate restTemplate;
    private String accessToken;
    private String url;


    public GatherGitHubData() {
        restTemplate = new RestTemplate();
        url = "https://api.github.com/repos/tjjohn1/cassess-capstone-team2/";
        GitHubProperties gitHubProperties = new GitHubProperties();
        accessToken = gitHubProperties.getAccessToken();
    }

    /**
     * Implements the GatherData interface method
     */
    @Override
    public void fetchData(){
        getBranches();
    }

    private void getBranches(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "branches")
                .queryParam("access_token", accessToken);

        String urlPath = builder.build().toUriString();

        String json = restTemplate.getForObject(urlPath, String.class);
        ArrayList<GitHubBranches> branches = null;

        ObjectMapper mapper = new ObjectMapper();

        try{
            branches = mapper.readValue(json, new TypeReference<ArrayList<GitHubBranches>>(){});
        }catch(IOException e){
            e.printStackTrace();
        }

        getAllCommits(branches);
    }

    /**
     * Gathers all commits to the repository, gathers their Sha IDs, and passes them to the getCommit method
     */
    private void getAllCommits(ArrayList<GitHubBranches> branches){
        ArrayList<GitHubSha> gitHubAllCommits = null;

        for(GitHubBranches branch: branches) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "commits")
                    .queryParam("sha", branch.getName())
                    .queryParam("access_token", accessToken);

            String urlPath = builder.build().toUriString();

            String json = restTemplate.getForObject(urlPath, String.class);
            ObjectMapper mapper = new ObjectMapper();


            ArrayList<GitHubSha> tempCommitList = null;
            try {
                tempCommitList = mapper.readValue(json, new TypeReference<ArrayList<GitHubSha>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(gitHubAllCommits == null){
                gitHubAllCommits = tempCommitList;
            }else{
                gitHubAllCommits.addAll(tempCommitList);
            }

        }

        getCommit(gitHubAllCommits);
    }

    /**
     * Takes the Sha IDs and gathers the relevant data for each commit
     * @param gitHubCommits     Arraylist full of Sha Keys
     */
    private void getCommit(ArrayList<GitHubSha> gitHubCommits){
        for (GitHubSha sha: gitHubCommits) {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "commits/" + sha.getSha() + "?access_token=" + accessToken);

            String urlPath = builder.build().toUriString();

            GitHubCommitData json = restTemplate.getForObject(urlPath, GitHubCommitData.class);

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