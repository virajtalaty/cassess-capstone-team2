package edu.asu.cassess.web.controller;

import edu.asu.cassess.dao.github.IGitHubCommitDataDao;
import edu.asu.cassess.model.Taiga.CourseList;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.service.github.IGatherGitHubData;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.ITeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.cassess.service.taiga.ITaskDataService;

import javax.ejb.EJB;
import java.util.List;

@RestController
@PropertySource("classpath:scheduling.properties")
public class TaskController {

	@Autowired
	ITaskDataService taigaDataService;

    @EJB
    private ICourseService coursesService;

    @EJB
    private ITeamsService teamsService;

    @Autowired
    private IGitHubCommitDataDao commitDao;

    @Autowired
    private IGatherGitHubData gatherData;
	
	@Scheduled(cron = "${taiga.cron.expression}")
	public void TaigaTasks() {
		System.out.println("cron ran as scheduled");
        List<Team> teams = teamsService.listReadAll();
        for(Team team: teams){
            Course course = (Course) coursesService.read(team.getCourse());
            gatherData.fetchData(course.getGithub_owner(), team.getGithub_repo_id());
        }
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            taigaDataService.updateTaskTotals(course.getCourse());
        }
	}
}
