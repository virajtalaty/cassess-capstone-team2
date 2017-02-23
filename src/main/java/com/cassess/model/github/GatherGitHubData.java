package com.cassess.model.github;

import com.cassess.entity.github.CommitData;
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
import java.util.List;

@Service
@Transactional
public class GatherGitHubData implements GatherData {

    @Autowired
    private GitHubCommitDataDao dao;

    private RestTemplate restTemplate;
    private String accessToken;
    private String url;
    private String projectName;
    private String courseName;


    public GatherGitHubData() {
        restTemplate = new RestTemplate();
        projectName = "cassess-capstone-team2/";
        courseName = "tjjohn1/";
        url = "https://api.github.com/repos/" + courseName + projectName;
        GitHubProperties gitHubProperties = new GitHubProperties();
        accessToken = gitHubProperties.getAccessToken();
    }

    /**
     * Implements the GatherData interface method
     */
    @Override
    public void fetchData(){
        //getBranches();
        getStats();
    }


    private void getStats(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "stats/contributors")
                .queryParam("access_token", accessToken);
        String urlPath = builder.build().toUriString();

        String json = restTemplate.getForObject(urlPath, String.class);

        ArrayList<GitHubContributors> contributors = null;
        ObjectMapper mapper = new ObjectMapper();

        try{
            contributors = mapper.readValue(json, new TypeReference<ArrayList<GitHubContributors>>() {});
        }catch(IOException e){
            e.printStackTrace();
        }

        storeStats(contributors);
    }

    private void storeStats(ArrayList<GitHubContributors> contributors){
        for(GitHubContributors contributor: contributors){
            ArrayList<GitHubContributors.Weeks> weeks = contributor.getWeeks();

            for(GitHubContributors.Weeks week: weeks){
                Date date = new Date(week.getW() * 1000L);
                int linesAdded = week.getA();
                int linesDeleted = week.getD();
                int commits = week.getC();
                String userName = contributor.getAuthor().getLogin();

                CommitData commitData = new CommitData(date, userName, linesAdded, linesDeleted, commits, projectName, courseName);
                dao.save(commitData);
            }
        }

    }

    /**
     * Gathers each branch in order to gather each commit data from them
     */
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
            if(dao.getCommit(sha.getSha()) == null){
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "commits/" + sha.getSha() + "?access_token=" + accessToken);
                String urlPath = builder.build().toUriString();
                GitHubCommitData json = restTemplate.getForObject(urlPath, GitHubCommitData.class);

                String message = json.getCommit().getMessage().toLowerCase();

                if(!message.contains("merge")){
                    String commitId = sha.getSha();
                    Date date = json.getCommit().getAuthor().getDate();
                    String email = json.getCommit().getAuthor().getEmail();
                    int linesOfCodeAdded = json.getStats().getAdditions();
                    int linesOfCodeDeleted = json.getStats().getDeletions();

                    //CommitData commitData = new CommitData(commitId, date, email,linesOfCodeAdded,linesOfCodeDeleted, projectName, courseName);

                    //dao.save(commitData);
                }
            }
        }
    }

    /**
     * Gathers all the rows in the commit_data table
     * @return      A list of CommitData Objects
     */
    public List<CommitData> getCommitList(){
        return dao.getAllCommitData();
    }
}