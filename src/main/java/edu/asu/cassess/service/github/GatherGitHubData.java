package edu.asu.cassess.service.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.cassess.dao.github.IGitHubCommitDataDao;
import edu.asu.cassess.model.github.GitHubAnalytics;
import edu.asu.cassess.persist.entity.github.CommitData;

import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.repo.github.CommitDataRepo;
import edu.asu.cassess.persist.repo.github.GitHubWeightRepo;
import edu.asu.cassess.service.rest.IStudentsService;
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
public class GatherGitHubData implements IGatherGitHubData {

    @Autowired
    private IGitHubCommitDataDao commitDao;

    @Autowired
    private GitHubWeightRepo weightRepo;

    @Autowired
    private CommitDataRepo commitDataRepo;

    @Autowired
    private IStudentsService studentsService;

    private RestTemplate restTemplate;
    private String url;
    private String projectName;
    private String owner;


    public GatherGitHubData() {
        restTemplate = new RestTemplate();
    }

    /**
     * Gathers the GitHub commit data from the GitHub Repo Stats
     * The github owner and project name can be found in the repo url as follows
     * www.github.com/:owner/:projectName
     *
     * @param owner       the owner of the repo
     * @param projectName the project name of the repo
     */
    @Override
    public void fetchData(String owner, String projectName, String course, String team, String accessToken){
        this.projectName = projectName;
        this.owner = owner;
        url = "https://api.github.com/repos/" + owner + "/" + projectName + "/";

        getStats(course, team, accessToken);
    }


    private void getStats(String course, String team, String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "stats/contributors")
                .queryParam("access_token", accessToken);
        String urlPath = builder.build().toUriString();

        String json = restTemplate.getForObject(urlPath, String.class);

        ArrayList<GitHubContributors> contributors = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            contributors = mapper.readValue(json, new TypeReference<ArrayList<GitHubContributors>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        storeStats(contributors, accessToken, course, team);
    }

    private void storeStats(ArrayList<GitHubContributors> contributors, String accessToken, String course, String team) {
        for (GitHubContributors contributor : contributors) {
            ArrayList<GitHubContributors.Weeks> weeks = contributor.getWeeks();

            for (GitHubContributors.Weeks week : weeks) {
                Date date = new Date(week.getW() * 1000L);
                int linesAdded = week.getA();
                int linesDeleted = week.getD();
                int commits = week.getC();
                String userName = contributor.getAuthor().getLogin();

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.github.com/users/" + userName)
                        .queryParam("access_token", accessToken);
                String urlPath = builder.build().toUriString();

                GitHubUser user = restTemplate.getForObject(urlPath, GitHubUser.class);
                String email = user.getEmail();

                if(email == null){
                    email = "hiddenEmail";
                    commitDataRepo.save(new CommitData(date, userName, email, linesAdded, linesDeleted, commits, projectName, owner, course, team));

                    int weight = GitHubAnalytics.calculateWeight(linesAdded, linesDeleted);
                    GitHubWeight gitHubWeight = new GitHubWeight(email, date, weight, userName, course, team);
                    weightRepo.save(gitHubWeight);
                }

                Student student = new Student();
                Object object = studentsService.find(email, team, course);
                if(object.getClass() == Student.class){
                    student = (Student) object;
                }
                if (student.getEnabled() != null) {
                    if (student.getEnabled() != false) {
                        commitDataRepo.save(new CommitData(date, userName, email, linesAdded, linesDeleted, commits, projectName, owner, course, team));

                        int weight = GitHubAnalytics.calculateWeight(linesAdded, linesDeleted);
                        GitHubWeight gitHubWeight = new GitHubWeight(email, date, weight, userName, course, team);
                        weightRepo.save(gitHubWeight);
                    }
                }
            }
        }
    }

    /**
     * Gathers all the rows in the commit_data table
     *
     * @return A list of CommitData Objects
     */
    @Override
    public List<CommitData> getCommitList(){
        return commitDao.getAllCommitData();
    }
}