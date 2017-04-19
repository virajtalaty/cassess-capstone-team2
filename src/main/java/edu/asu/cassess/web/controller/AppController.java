package edu.asu.cassess.web.controller;

import edu.asu.cassess.dao.github.IGitHubCommitDataDao;
import edu.asu.cassess.dao.github.IGitHubWeightDao;
import edu.asu.cassess.model.Taiga.*;
import edu.asu.cassess.persist.entity.github.CommitData;
import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.security.SecurityUtils;
import edu.asu.cassess.service.github.IGatherGitHubData;
import edu.asu.cassess.service.rest.*;
import edu.asu.cassess.service.taiga.IMembersService;
import edu.asu.cassess.service.taiga.IProjectService;
import edu.asu.cassess.service.taiga.ITaskDataService;
import edu.asu.cassess.dao.taiga.ITaskTotalsQueryDao;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Transactional
@RestController
@Api(description = "Internal Calls API")
public class AppController {


    @Autowired
    private ITaskTotalsQueryDao taskTotalService;

    @Autowired
    private ICourseService coursesService;

    @Autowired
    private IProjectService projects;

    @Autowired
    private IMembersService members;

    @Autowired
    private ITeamsService teamsService;

    @Autowired
    private IStudentsService studentsService;

    @Autowired
    private IAdminsService adminsService;

    @Autowired
    private ITaskDataService taskService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private IGitHubWeightDao weightDao;

    @Autowired
    private IGitHubCommitDataDao commitDao;

    @Autowired
    private IGatherGitHubData gatherData;

    //New Query Based method to retrieve the current User object, associated with the current login
    @ResponseBody
    @RequestMapping(value = "/current_user", method = RequestMethod.GET)
    public User getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        return securityUtils.getCurrentUser();
    }

    //New Query Based Method to get the course list for which an admin is assigned to
    @ResponseBody
    @RequestMapping(value = "/admin_courses", method = RequestMethod.GET)
    public List<CourseList> getAdminCourses(@RequestHeader(name = "email", required = true) String email,
                                              HttpServletRequest request, HttpServletResponse response) {
        return adminsService.listGetCoursesForAdmin(email);

    }

    //Previous Query Based method to obtain Teams assigned to a particular course

    //Gets the Teams/Projects which are assigned to a course
    @ResponseBody
    @RequestMapping(value = "/course_teams", method = RequestMethod.GET)
    public
    ResponseEntity<List<TeamNames>> getCourseTeams(@RequestHeader(name = "course", required = true) String course,
                                                   HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("Course: " + course);
        List<TeamNames> teamList = (List<TeamNames>) teamsService.listGetTeamNames(course);
        //for(TeamNames team:teamList){
        //System.out.print("Team: " + team.getTeam());
        //}
        return new ResponseEntity<List<TeamNames>>(teamList, HttpStatus.OK);
    }

    //Previous Query Based method to obtain Students assigned to a particular team/project
    @ResponseBody
    @RequestMapping(value = "/team_students", method = RequestMethod.GET)
    public
    ResponseEntity<List<Student>> getTeamStudents(@RequestHeader(name = "course", required = true) String course,
                                                  @RequestHeader(name = "team", required = true) String team,
                                                  HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("Team: " + team);
        List<Student> studentList = (List<Student>) studentsService.listReadByTeam(course, team);
        //for(Student student:studentList){
        //System.out.print("Student: " + student.getFull_name());
        //}
        return new ResponseEntity<List<Student>>(studentList, HttpStatus.OK);
    }

    //New Query Based Methods to get the courses and projects lists for which a student is assigned to

    //Gets courses which the student is assigned to
    @ResponseBody
    @RequestMapping(value = "/student_courses", method = RequestMethod.GET)
    public List<CourseList> getStudentCourses(@RequestHeader(name = "email", required = true) String email,
                                              HttpServletRequest request, HttpServletResponse response) {
        return studentsService.listGetCoursesForStudent(email);

    }

    //Gets Projects assigned to a student
    @ResponseBody
    @RequestMapping(value = "/assigned_team", method = RequestMethod.GET)
    public List<TeamNames> getAssignedTeams(@RequestHeader(name = "email", required = true) String email,
                                   HttpServletRequest request, HttpServletResponse response) {

        return studentsService.listGetAssignedTeams(email);
    }
    //End of New Student Course and Project list methods


    //---------------------------------------------------------------------------------------------



    //New Charting Query Based Methods for Sprint 4

    //Daily task totals for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_tasks", method = RequestMethod.GET)
    public
    ResponseEntity<List<DailyTaskTotals>> getStudentTasks(@RequestHeader(name = "course", required = true) String course,
                                                          @RequestHeader(name = "team", required = true) String team,
                                                          @RequestHeader(name = "email", required = true) String email,
                                                          @RequestHeader(name = "weekBeginning", required = true) String weekBeginning,
                                                          @RequestHeader(name = "weekEnding", required = true) String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByStudent(weekBeginning, weekEnding, course, team, email);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Daily task totals for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_tasks", method = RequestMethod.GET)
    public
    ResponseEntity<List<DailyTaskTotals>> getAverageTeamTasks(@RequestHeader(name = "course", required = true) String course,
                                                              @RequestHeader(name = "team", required = true) String team,
                                                              @RequestHeader(name = "weekBeginning", required = true) String weekBeginning,
                                                              @RequestHeader(name = "weekEnding", required = true) String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByTeam(weekBeginning, weekEnding, course, team);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Daily task totals for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_tasks", method = RequestMethod.GET)
    public
    ResponseEntity<List<DailyTaskTotals>> getAverageCourseTasks(@RequestHeader(name = "course", required = true) String course,
                                                          @RequestHeader(name = "weekBeginning", required = true) String weekBeginning,
                                                          @RequestHeader(name = "weekEnding", required = true) String weekEnding,
                                                                HttpServletRequest request, HttpServletResponse response) {
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByCourse(weekBeginning, weekEnding, course);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Weekly Activity for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_activity", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyActivity>> getStudentActivity(@RequestHeader(name = "course", required = true) String course,
                                                            @RequestHeader(name = "team", required = true) String team,
                                                            @RequestHeader(name = "email", required = true) String email,
                                                            HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Activity for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_activity", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyActivity>> getProjectActivity(@RequestHeader(name = "course", required = true) String course,
                                                            @RequestHeader(name = "team", required = true) String team,
                                                            HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByTeam(course, team);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Activity for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_activity", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyActivity>> getCourseActivity(@RequestHeader(name = "course", required = true) String course,
                                                           HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByCourse(course);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_intervals", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyIntervals>> getProjectIntervals(@RequestHeader(name = "course", required = true) String course,
                                                              @RequestHeader(name = "team", required = true) String team,
                                                                  HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_intervals", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyIntervals>> getStudentIntervals(@RequestHeader(name = "course", required = true) String course,
                                                              @RequestHeader(name = "team", required = true) String team,
                                                              @RequestHeader(name = "email", required = true) String email,
                                                              String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly weights for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_weightFreq", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyFreqWeight>> getStudentWeightFreq(@RequestHeader(name = "course", required = true) String course,
                                                            @RequestHeader(name = "team", required = true) String team,
                                                            @RequestHeader(name = "email", required = true) String email,
                                                            String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.weeklyWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Weekly average weights for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_weightFreq", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyFreqWeight>> getProjectWeight(@RequestHeader(name = "course", required = true) String course,
                                                            @RequestHeader(name = "team", required = true) String team,
                                                            String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.weeklyWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Weekly average weights for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_weightFreq", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyFreqWeight>> getCourseWeight(@RequestHeader(name = "course", required = true) String course,
                                                           String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.weeklyWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week weight for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_quickweightFreq", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyFreqWeight>> twoWeekStudentWeightFreq(@RequestHeader(name = "course", required = true) String course,
                                                                    @RequestHeader(name = "team", required = true) String team,
                                                                    @RequestHeader(name = "email", required = true) String email,
                                                                    String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week average weight for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_quickweightFreq", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyFreqWeight>> twoWeekProjectWeightFreq(@RequestHeader(name = "course", required = true) String course,
                                                                    @RequestHeader(name = "team", required = true) String team,
                                                                    String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week average weight for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_quickweightFreq", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyFreqWeight>> twoWeekCourseWeightFreq(@RequestHeader(name = "course", required = true) String course,
                                                                   String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Weekly task status averages for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_average", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> getStudentAverage(@RequestHeader(name = "course", required = true) String course,
                                                        @RequestHeader(name = "team", required = true) String team,
                                                        @RequestHeader(name = "email", required = true) String email,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Weekly task status averages for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_average", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> getProjectAverage(@RequestHeader(name = "course", required = true) String course,
                                                        @RequestHeader(name = "team", required = true) String team,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByTeam(course, team);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Weekly task status averages for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_average", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> getCourseAverage(@RequestHeader(name = "course", required = true) String course,
                                                       String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByCourse(course);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_quickaverage", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> lastTwoWeekStudentAverage(@RequestHeader(name = "course", required = true) String course,
                                                                @RequestHeader(name = "team", required = true) String team,
                                                                @RequestHeader(name = "email", required = true) String email,
                                                                String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_quickaverage", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> lastTwoWeekProjectAverage(@RequestHeader(name = "course", required = true) String course,
                                                                @RequestHeader(name = "team", required = true) String team,
                                                                String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByTeam(course, team);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_quickaverage", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> lastTwoWeekCourseAverage(@RequestHeader(name = "course", required = true) String course,
                                                               String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByCourse(course);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //End of New Charting Methods for Sprint 4

    //-----------------------------------------------------------------------------------


    //Previous Query Based method to obtain the courses currently in the Database
    //For Admins not assigned to a particular course, but system-Admins
    @RequestMapping(value = "/taigaCourses", method = RequestMethod.GET)
    public
    ResponseEntity<List<CourseList>> getCourses(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = (List<CourseList>) coursesService.listGetCourses();
        return new ResponseEntity<List<CourseList>>(courseList, HttpStatus.OK);
    }


    @RequestMapping(value = "/taiga/Update/Projects", method = RequestMethod.POST)
    public
    void updateProjects(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            projects.updateProjects(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Memberships", method = RequestMethod.POST)
    public
    void updateMemberships(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            members.updateMembership(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Tasks", method = RequestMethod.POST)
    public
    void updateTasks(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            taskService.updateTaskTotals(course.getCourse());
        }
    }

    //---------- GitHub Routes --------------

    //GET the weights for the selected email
    @RequestMapping(value = "github/weight", method = RequestMethod.GET)
    public
    ResponseEntity<List<GitHubWeight>> getWeight(@RequestHeader(name = "email") String email,
                   HttpServletRequest request, HttpServletResponse response){
        List<GitHubWeight> weightList = weightDao.getWeightByEmail(email);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }

    @RequestMapping(value = "github/weight", method = RequestMethod.POST)
    public
    void updateGitHubData(HttpServletRequest request, HttpServletResponse response){
        List<Team> teams = teamsService.listReadAll();
        for(Team team: teams){
            Course course = (Course) coursesService.read(team.getCourse());
            gatherData.fetchData(course.getGithub_owner(), team.getGithub_repo_id());
        }
    }

    //GET the commits for the selected email
    @RequestMapping(value = "github/commits", method = RequestMethod.GET)
    public
    ResponseEntity<List<CommitData>> getCommits(@RequestHeader(name = "email") String email,
                                                HttpServletRequest request, HttpServletResponse response){
        List<CommitData> commitList = commitDao.getCommitByEmail(email);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }
}