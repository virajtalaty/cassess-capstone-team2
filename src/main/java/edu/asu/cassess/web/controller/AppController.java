package edu.asu.cassess.web.controller;

import edu.asu.cassess.config.ServiceConfig;
import edu.asu.cassess.dao.github.IGitHubCommitDataDao;
import edu.asu.cassess.dao.github.IGitHubCommitQueryDao;
import edu.asu.cassess.dao.github.IGitHubWeightDao;
import edu.asu.cassess.dao.github.IGitHubWeightQueryDao;
import edu.asu.cassess.dao.slack.IConsumeUsers;
import edu.asu.cassess.dao.slack.ISlackMessageDao;
import edu.asu.cassess.dao.slack.ISlackMessageTotalsQueryDao;
import edu.asu.cassess.dao.slack.IUserObjectQueryDao;
import edu.asu.cassess.dao.taiga.IMemberQueryDao;
import edu.asu.cassess.dao.taiga.IProjectQueryDao;
import edu.asu.cassess.dao.taiga.ITaskTotalsQueryDao;
import edu.asu.cassess.model.Taiga.*;
import edu.asu.cassess.model.github.PeriodicGithubActivity;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.model.slack.DailyMessageTotals;
import edu.asu.cassess.model.slack.WeeklyMessageTotals;
import edu.asu.cassess.persist.entity.github.CommitData;
import edu.asu.cassess.persist.entity.github.GitHubWeight;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.entity.slack.UserObject;
import edu.asu.cassess.persist.repo.UserRepo;
import edu.asu.cassess.persist.repo.rest.StudentRepo;
import edu.asu.cassess.security.SecurityUtils;
import edu.asu.cassess.service.github.IGatherGitHubData;
import edu.asu.cassess.service.github.agReplacement.Github;
import edu.asu.cassess.service.rest.*;
import edu.asu.cassess.service.security.IUserService;
import edu.asu.cassess.service.slack.IChannelHistoryService;
import edu.asu.cassess.service.taiga.IMembersService;
import edu.asu.cassess.service.taiga.IProjectService;
import edu.asu.cassess.service.taiga.ITaskDataService;
import io.swagger.annotations.Api;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static javax.ws.rs.core.HttpHeaders.USER_AGENT;

@Transactional
@RestController
@Api(description = "Internal Calls API")
public class AppController {

    @Autowired
    private IConsumeUsers consumeUsers;

    @Autowired
    private IGitHubWeightQueryDao gitHubWeightQueryDao;

    @Autowired
    private IChannelHistoryService channelHistoryService;

    @Autowired
    private ISlackMessageTotalsQueryDao slackMessageTotalsService;

    @Autowired
    private IGitHubCommitQueryDao gitHubQueryDao;

    @Autowired
    private ITaskTotalsQueryDao taskTotalService;

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

    @EJB
    private ICourseService courseService;

    @EJB
    private ITeamsService teamService;

    @EJB
    private IStudentsService studentService;

    @EJB
    private IAdminsService adminService;

    @EJB
    private IUserService usersService;

    @EJB
    private IChannelService channelService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StudentRepo studentRepo;

    @EJB
    private IProjectQueryDao projectDao;

    @EJB
    private IMemberQueryDao memberDao;

    @EJB
    private IMembersService members;

    @EJB
    private IProjectService projects;

    @Autowired
    ISlackMessageDao slackMessageDao;

    @Autowired
    IUserObjectQueryDao userObjectQueryDao;

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
        return adminService.listGetCoursesForAdmin(email);

    }

    @ResponseBody
    @RequestMapping(value = "/course_students", method = RequestMethod.GET)
    public List<Student> getCourseStudents(@RequestHeader(name = "course", required = true) String course,
                                            HttpServletRequest request, HttpServletResponse response) {
        return studentService.listReadByCourse(course);

    }

    @ResponseBody
    @RequestMapping(value = "/course_data", method = RequestMethod.GET)
    public Course getCoursePeriod(@RequestHeader(name = "course", required = true) String course,
                                           HttpServletRequest request, HttpServletResponse response) {
        return (Course)courseService.read(course);

    }

    //Previous Query Based method to obtain Teams assigned to a particular course

    //Gets the Teams/Projects which are assigned to a course
    @ResponseBody
    @RequestMapping(value = "/course_teams", method = RequestMethod.GET)
    public ResponseEntity<List<TeamNames>> getCourseTeams(@RequestHeader(name = "course", required = true) String course,
                                                          HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("Course: " + course);
        List<TeamNames> teamList = (List<TeamNames>) teamService.listGetTeamNames(course);
        //for(TeamNames team:teamList){
        //System.out.print("Team: " + team.getTeam());
        //}
        return new ResponseEntity<List<TeamNames>>(teamList, HttpStatus.OK);
    }
    //Get the URL to the detailed Github Activity for a team
    @RequestMapping(value = "/github/daily_activity_json", method = RequestMethod.GET)
    public String listGetJSONGithubActivityURL(@RequestHeader(name = "course", required = true) String course,
                                               @RequestHeader(name = "team", required = true) String team,
                                               @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                               @RequestHeader(name = "weekEnding", required = true) long weekEnding,
                                               HttpServletRequest request, HttpServletResponse response) {
        PeriodicGithubActivity weightList = teamService.listGetDetailedGithubActivityURL(course, team);

        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);

        String jsonURL = weightList.getGithub_activity_URL()+"&start_date="+formattedDateBegin+"&end_date="+formattedDateEnd;
        StringBuffer response1 = new StringBuffer();
        System.out.println("URL - Created - " + jsonURL);
        String jsonData = teamService.getAGGithubData(jsonURL);

        if(jsonData.equals("-1")) {
            try {
                /*URL obj = new URL(jsonURL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response1.append(inputLine);
                }
                in.close();
                jsonData = response1.toString();*/
                String[] parts = teamService.GetAgParts(course,team);
                Github gitData = new Github(parts[2],parts[3],parts[0],parts[1].split("%2C"));
                jsonData = gitData.getComitDataAll(formattedDateBegin,formattedDateEnd);


                teamService.updateGithubAG(jsonURL,jsonData);
             } catch (Exception e) {
                System.out.println("Unsuccessful");
            }
        }
        return jsonData;
    }

    //Previous Query Based method to obtain Students assigned to a particular team/project
    @ResponseBody
    @RequestMapping(value = "/team_students", method = RequestMethod.GET)
    public ResponseEntity<List<Student>> getTeamStudents(@RequestHeader(name = "course", required = true) String course,
                                                         @RequestHeader(name = "team", required = true) String team,
                                                         HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("Team: " + team);
        List<Student> studentList = (List<Student>) studentService.listReadByTeam(course, team);
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
        return studentService.listGetCoursesForStudent(email);

    }

    //Gets Teams assigned to a student
    @ResponseBody
    @RequestMapping(value = "/student_teams", method = RequestMethod.GET)
    public List<TeamNames> getAssignedTeams(@RequestHeader(name = "email", required = true) String email,
                                            @RequestHeader(name = "course", required = true) String course,
                                            HttpServletRequest request, HttpServletResponse response) {

        return studentService.listGetAssignedTeams(email, course);
    }

    @ResponseBody
    @RequestMapping(value = "/student_data", method = RequestMethod.GET)
    public List<Student> getStudent(@RequestHeader(name = "email", required = true) String email,
                                    @RequestHeader(name = "team", required = true) String team,
                                    @RequestHeader(name = "course", required = true) String course,
                                    HttpServletRequest request, HttpServletResponse response) {

        return studentService.listReadSingleStudent(course, team, email);
    }


    //End of New Student Course and Project list methods


    //---------------------------------------------------------------------------------------------


    //New Taiga Charting Query Based Methods for Sprint 4

    //Daily task totals for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_tasks", method = RequestMethod.GET)
    public ResponseEntity<List<DailyTaskTotals>> getStudentTasks(@RequestHeader(name = "course", required = true) String course,
                                                                 @RequestHeader(name = "team", required = true) String team,
                                                                 @RequestHeader(name = "email", required = true) String email,
                                                                 @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                 @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        Date dateBegin = new Date(weekBeginning); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByStudent(formattedDateBegin, formattedDateEnd, course, team, email);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Daily task totals for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_tasks", method = RequestMethod.GET)
    public ResponseEntity<List<DailyTaskTotals>> getAverageTeamTasks(@RequestHeader(name = "course", required = true) String course,
                                                                     @RequestHeader(name = "team", required = true) String team,
                                                                     @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                     @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        //System.out.print("-------------------------------------------------------------DateBeginning: " + formattedDateBegin);
        //System.out.print("-------------------------------------------------------------DateEnd: " + formattedDateEnd);
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByTeam(formattedDateBegin, formattedDateEnd, course, team);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Daily task totals for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_tasks", method = RequestMethod.GET)
    public ResponseEntity<List<DailyTaskTotals>> getAverageCourseTasks(@RequestHeader(name = "course", required = true) String course,
                                                                       @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                       @RequestHeader(name = "weekEnding", required = true) long weekEnding,
                                                                       HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        //System.out.print("-------------------------------------------------------------DateBeginning: " + formattedDateBegin);
        //System.out.print("-------------------------------------------------------------DateEnd: " + formattedDateEnd);
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByCourse(formattedDateBegin, formattedDateEnd, course);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Weekly Activity for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_activity", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyActivity>> getStudentActivity(@RequestHeader(name = "course", required = true) String course,
                                                                   @RequestHeader(name = "team", required = true) String team,
                                                                   @RequestHeader(name = "email", required = true) String email,
                                                                   HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Activity for a team
    @ResponseBody
    @RequestMapping(value = "/taiga/team_activity", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyActivity>> getTeamActivity(@RequestHeader(name = "course", required = true) String course,
                                                                @RequestHeader(name = "team", required = true) String team,
                                                                HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByTeam(course, team);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Activity for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_activity", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyActivity>> getCourseActivity(@RequestHeader(name = "course", required = true) String course,
                                                                  HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyActivity> activityList = (List<WeeklyActivity>) taskTotalService.getWeeklyUpdatesByCourse(course);
        return new ResponseEntity<List<WeeklyActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/course_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getTaigaCourseIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                         HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByCourse(course);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getTaigaTeamIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                       @RequestHeader(name = "team", required = true) String team,
                                                                       HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getTaigaStudentIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                          @RequestHeader(name = "team", required = true) String team,
                                                                          @RequestHeader(name = "email", required = true) String email,
                                                                          String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly weights for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> getTaigaStudentWeightFreq(@RequestHeader(name = "course", required = true) String course,
                                                                            @RequestHeader(name = "team", required = true) String team,
                                                                            @RequestHeader(name = "email", required = true) String email,
                                                                            @RequestHeader(name="weekBeginning", required=true) long weekBeginning,
                                                                            @RequestHeader(name="weekEnding", required=true) long weekEnding,
                                                                            HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.taskTotalService.weeklyWeightFreqByStudent(course, team, email, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.taskTotalService.weeklyWeightFreqByStudent(course, team, email, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);

    }

    //Weekly average weights for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> getTaigaTeamWeight(@RequestHeader(name = "course", required = true) String course,
                                                                     @RequestHeader(name = "team", required = true) String team,
                                                                     @RequestHeader(name="weekBeginning", required=true) long weekBeginning,
                                                                     @RequestHeader(name="weekEnding", required=true) long weekEnding,
                                                                     HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.taskTotalService.weeklyWeightFreqByTeam(course, team, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.taskTotalService.weeklyWeightFreqByTeam(course, team, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);

    }

    //Weekly average weights for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> getTaigaCourseWeight(@RequestHeader(name = "course", required = true) String course,
                                                                       @RequestHeader(name="weekBeginning", required=true) long weekBeginning,
                                                                       @RequestHeader(name="weekEnding", required=true) long weekEnding,
                                                                       HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.taskTotalService.weeklyWeightFreqByCourse(course, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.taskTotalService.weeklyWeightFreqByCourse(course, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);

    }

    //Current and last week Taiga weight for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_quickweightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekStudentWeightFreqTG(@RequestHeader(name = "course", required = true) String course,
                                                                             @RequestHeader(name = "team", required = true) String team,
                                                                             @RequestHeader(name = "email", required = true) String email,
                                                                             String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Taiga average weight for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_quickweightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekTeamWeightFreqTG(@RequestHeader(name = "course", required = true) String course,
                                                                          @RequestHeader(name = "team", required = true) String team,
                                                                          String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Taiga average weight for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_quickweightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekCourseWeightFreqTG(@RequestHeader(name = "course", required = true) String course,
                                                                            String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) taskTotalService.twoWeekWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/github/student_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekStudentWeightFreqGH(@RequestHeader(name = "course", required = true) String course,
                                                                             @RequestHeader(name = "team", required = true) String team,
                                                                             @RequestHeader(name = "email", required = true) String email,
                                                                             @RequestHeader(name="weekBeginning", required=true) long weekBeginning,
                                                                             @RequestHeader(name="weekEnding", required=true) long weekEnding,
                                                                             HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.gitHubQueryDao.getWeeklyWeightFreqByStudent(course, team, email, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.gitHubQueryDao.getWeeklyWeightFreqByStudent(course, team, email, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/github/team_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekProjectWeightFreqGH(@RequestHeader(name = "course", required = true) String course,
                                                                             @RequestHeader(name = "team", required = true) String team,
                                                                             @RequestHeader(name="weekBeginning", required=true) long weekBeginning,
                                                                             @RequestHeader(name="weekEnding", required=true) long weekEnding,
                                                                             HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.gitHubQueryDao.getWeeklyWeightFreqByTeam(course, team, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.gitHubQueryDao.getWeeklyWeightFreqByTeam(course, team, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/github/course_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekCourseWeightFreqGH(@RequestHeader(name = "course", required = true) String course,
                                                                            @RequestHeader(name="weekBeginning", required=true) long weekBeginning,
                                                                            @RequestHeader(name="weekEnding", required=true) long weekEnding,
                                                                            HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.gitHubQueryDao.getWeeklyWeightFreqByCourse(course, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.gitHubQueryDao.getWeeklyWeightFreqByCourse(course, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);
    }

    //Weekly task status averages for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_average", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyAverages>> getStudentAverage(@RequestHeader(name = "course", required = true) String course,
                                                                  @RequestHeader(name = "team", required = true) String team,
                                                                  @RequestHeader(name = "email", required = true) String email,
                                                                  String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Weekly task status averages for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_average", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyAverages>> getProjectAverage(@RequestHeader(name = "course", required = true) String course,
                                                                  @RequestHeader(name = "team", required = true) String team,
                                                                  String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByTeam(course, team);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Weekly task status averages for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_average", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyAverages>> getCourseAverage(@RequestHeader(name = "course", required = true) String course,
                                                                 String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByCourse(course);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_quickaverage", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyAverages>> lastTwoWeekStudentAverage(@RequestHeader(name = "course", required = true) String course,
                                                                          @RequestHeader(name = "team", required = true) String team,
                                                                          @RequestHeader(name = "email", required = true) String email,
                                                                          String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/team_quickaverage", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyAverages>> lastTwoWeekProjectAverage(@RequestHeader(name = "course", required = true) String course,
                                                                          @RequestHeader(name = "team", required = true) String team,
                                                                          String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByTeam(course, team);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_quickaverage", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyAverages>> lastTwoWeekCourseAverage(@RequestHeader(name = "course", required = true) String course,
                                                                         String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByCourse(course);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }


    //End of New Taiga Charting Methods for Sprint 4

    //-----------------------------------------------------------------------------------


    //Previous Query Based method to obtain the courses currently in the Database
    //For Admins not assigned to a particular course, but system-Admins
    @RequestMapping(value = "/taigaCourses", method = RequestMethod.GET)
    public ResponseEntity<List<CourseList>> getCourses(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = (List<CourseList>) courseService.listGetCourses();
        return new ResponseEntity<List<CourseList>>(courseList, HttpStatus.OK);
    }


    @RequestMapping(value = "/taiga/Update/Projects", method = RequestMethod.POST)
    public void updateTaigaProjects(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            projects.updateProjects(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Memberships", method = RequestMethod.POST)
    public void updateTaigaMemberships(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            members.updateMembership(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Tasks", method = RequestMethod.POST)
    public void updateTaigaTasks(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            taskService.updateTaskTotals(course.getCourse());
        }
    }

    //---------- Slack Routes -----------------

    @RequestMapping(value = "/slack/update_users", method = RequestMethod.POST)
    public void updateSlackUses(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            consumeUsers.updateSlackUsers(course.getCourse());
        }
    }

    @RequestMapping(value = "/slack/update_messageTotals", method = RequestMethod.POST)
    public void updateSlackMessageTotals(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = courseService.listGetCourses();
        for (CourseList course : courseList) {
            //System.out.print("Course: " + course.getCourse());
            channelHistoryService.updateMessageTotals(course.getCourse());
        }
    }

    //------------- Slack Charting Methods --------------

    //Daily Message Counts for a course
    @ResponseBody
    @RequestMapping(value = "/slack/course_messages", method = RequestMethod.GET)
    public ResponseEntity<List<DailyMessageTotals>> getSlackCourseMessageCounts(@RequestHeader(name = "course", required = true) String course,
                                                                                @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                                @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        List<DailyMessageTotals> countList = slackMessageDao.getCourseDailyTotals(course,weekBeginning,weekEnding);//(List<DailyMessageTotals>) slackMessageTotalsService.getDailyCountsByCourse(formattedDateBegin, formattedDateEnd, course);
        return new ResponseEntity<List<DailyMessageTotals>>(countList, HttpStatus.OK);
    }

    //Daily Message Counts for a team
    @ResponseBody
    @RequestMapping(value = "/slack/team_messages", method = RequestMethod.GET)
    public ResponseEntity<List<DailyMessageTotals>> getSlackTeamMessageCounts(@RequestHeader(name = "course", required = true) String course,
                                                                              @RequestHeader(name = "team", required = true) String team,
                                                                              @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                              @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        List<DailyMessageTotals> countList = slackMessageDao.getTeamDailyTotals(course,team,weekBeginning,weekEnding);//List<DailyMessageTotals>) slackMessageTotalsService.getDailyCountsByTeam(formattedDateBegin, formattedDateEnd, course, team);
        return new ResponseEntity<List<DailyMessageTotals>>(countList, HttpStatus.OK);

    }

    //Daily Message Counts for a student
    @ResponseBody
    @RequestMapping(value = "/slack/student_messages", method = RequestMethod.GET)
    public ResponseEntity<List<DailyMessageTotals>> getSlackStudentMessageCounts(@RequestHeader(name = "course", required = true) String course,
                                                                                 @RequestHeader(name = "team", required = true) String team,
                                                                                 @RequestHeader(name = "email", required = true) String email,
                                                                                 @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                                 @RequestHeader(name = "weekEnding", required = true) long weekEnding, HttpServletRequest request, HttpServletResponse response) {

        String student = ((UserObject)userObjectQueryDao.getUserByEmail(course,email)).getId();
        List<DailyMessageTotals> countList = slackMessageDao.getStudentDailyTotals (course,team,student,weekBeginning,weekEnding);//List<DailyMessageTotals>) slackMessageTotalsService.getDailyCountsByStudent(formattedDateBegin, formattedDateEnd, course, team, email);
        return new ResponseEntity<List<DailyMessageTotals>>(countList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/slack/course_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getSlackCourseIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                         HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) slackMessageTotalsService.getWeeklyIntervalsByCourse(course);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/slack/team_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getSlackTeamIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                       @RequestHeader(name = "team", required = true) String team,
                                                                       HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) slackMessageTotalsService.getWeeklyIntervalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @RequestMapping(value = "/slack/student_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getSlackStudentIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                          @RequestHeader(name = "team", required = true) String team,
                                                                          @RequestHeader(name = "email", required = true) String email,
                                                                          String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) slackMessageTotalsService.getWeeklyIntervalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Message Totals for a student
    @ResponseBody
    @RequestMapping(value = "/slack/student_totals", method = RequestMethod.GET)
    public ResponseEntity<List<DailyMessageTotals>> getStudentMessageTotals(@RequestHeader(name = "course", required = true) String course,
                                                                             @RequestHeader(name = "team", required = true) String team,
                                                                             @RequestHeader(name = "email", required = true) String email,
                                                                             HttpServletRequest request, HttpServletResponse response) {
        Course c = (Course)courseService.read(course);
        List<DailyMessageTotals> activityList = slackMessageDao.getStudentDailyTotals(course,team,email,c.getStart_date().getTime(),c.getEnd_date().getTime());
        return new ResponseEntity<List<DailyMessageTotals>>(activityList, HttpStatus.OK);
    }

    //Weekly Message Totals for a team
    @ResponseBody
    @RequestMapping(value = "/slack/team_totals", method = RequestMethod.GET)
    public ResponseEntity<List<DailyMessageTotals>> getTeamMessageTotals(@RequestHeader(name = "course", required = true) String course,
                                                                          @RequestHeader(name = "team", required = true) String team,
                                                                          HttpServletRequest request, HttpServletResponse response) {
        Course c = (Course)courseService.read(course);
        List<DailyMessageTotals> totalsList = slackMessageDao.getTeamDailyTotals(course,team,c.getStart_date().getTime(),c.getEnd_date().getTime());
        return new ResponseEntity<List<DailyMessageTotals>>(totalsList, HttpStatus.OK);
    }

    //Weekly Message Totals for a course
    @ResponseBody
    @RequestMapping(value = "/slack/course_totals", method = RequestMethod.GET)
    public ResponseEntity<List<DailyMessageTotals>> getCourseMessageTotals(@RequestHeader(name = "course", required = true) String course,
                                                                            HttpServletRequest request, HttpServletResponse response) {
        Course c = (Course)courseService.read(course);
        List<DailyMessageTotals> totalsList = slackMessageDao.getCourseDailyTotals(course,c.getStart_date().getTime(),c.getEnd_date().getTime());
        return new ResponseEntity<List<DailyMessageTotals>>(totalsList, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/slack/student_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> getSlackStudentWeightFreq(@RequestHeader(name="course", required=true) String course, @RequestHeader(name="team", required=true) String team, @RequestHeader(name="email", required=true) String email, @RequestHeader(name="weekBeginning", required=true) long weekBeginning, @RequestHeader(name="weekEnding", required=true) long weekEnding, HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.slackMessageTotalsService.weeklyWeightFreqByStudent(course, team, email, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.slackMessageTotalsService.weeklyWeightFreqByStudent(course, team, email, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/slack/team_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> getSlackTeamWeightFreq(@RequestHeader(name="course", required=true) String course, @RequestHeader(name="team", required=true) String team, @RequestHeader(name="weekBeginning", required=true) long weekBeginning, @RequestHeader(name="weekEnding", required=true) long weekEnding, HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.slackMessageTotalsService.weeklyWeightFreqByTeam(course, team, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.slackMessageTotalsService.weeklyWeightFreqByTeam(course, team, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/slack/course_weightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> getSlackCourseWeightFreq(@RequestHeader(name="course", required=true) String course,
                                                                           @RequestHeader(name="weekBeginning", required=true) long weekBeginning,
                                                                           @RequestHeader(name="weekEnding", required=true) long weekEnding,
                                                                           HttpServletRequest request, HttpServletResponse response)
    {
        Course courseObj = (Course)this.courseService.read(course);
        Date dateBegin = new Date(weekBeginning);
        Date dateEnd = new Date(weekEnding);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        String courseDateBegin = sdfBegin.format(courseObj.getStart_date());
        String courseDateEnd = sdfEnd.format(courseObj.getEnd_date());
        List<WeeklyFreqWeight> totalWeightFreqList = this.slackMessageTotalsService.weeklyWeightFreqByCourse(course, courseDateBegin, courseDateEnd);
        List<WeeklyFreqWeight> weightFreqList = this.slackMessageTotalsService.weeklyWeightFreqByCourse(course, formattedDateBegin, formattedDateEnd);
        double freqTot = 0.0D;
        double weightTot = 0.0D;
        int count = 0;
        for (int i = 0; i < weightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(weightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(weightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight firstEntry = new WeeklyFreqWeight("1", formattedDateBegin, formattedDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        freqTot = 0.0D;
        weightTot = 0.0D;
        count = 0;
        for (int i = 0; i < totalWeightFreqList.size(); i++)
        {
            freqTot += Double.parseDouble(totalWeightFreqList.get(i).getFrequency());
            weightTot += Double.parseDouble(totalWeightFreqList.get(i).getWeight());
            count++;
        }
        WeeklyFreqWeight secondEntry = new WeeklyFreqWeight("2", courseDateBegin, courseDateEnd, String.format("%.3f", (freqTot/count)), String.format("%.3f", (weightTot/count)));
        return new ResponseEntity<List<WeeklyFreqWeight>>(new ArrayList<WeeklyFreqWeight>() {{
            add(firstEntry);
            add(secondEntry);
        }}, HttpStatus.OK);
    }

    //Current and last week Slack weight/frequency for a student
    @ResponseBody
    @RequestMapping(value = "/slack/student_quickweightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekStudentWeightFreqSK(@RequestHeader(name = "course", required = true) String course,
                                                                             @RequestHeader(name = "team", required = true) String team,
                                                                             @RequestHeader(name = "email", required = true) String email,
                                                                             String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.twoWeekWeightFreqByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Slack average weight/frequency for a project
    @ResponseBody
    @RequestMapping(value = "/slack/team_quickweightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekTeamWeightFreqSK(@RequestHeader(name = "course", required = true) String course,
                                                                          @RequestHeader(name = "team", required = true) String team,
                                                                          String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.twoWeekWeightFreqByTeam(course, team);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //Current and last week Taiga average weight/frequency for a course
    @ResponseBody
    @RequestMapping(value = "/slack/course_quickweightFreq", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyFreqWeight>> twoWeekCourseWeightFreqSK(@RequestHeader(name = "course", required = true) String course,
                                                                            String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyFreqWeight> weightFreqList = (List<WeeklyFreqWeight>) slackMessageTotalsService.twoWeekWeightFreqByCourse(course);
        return new ResponseEntity<List<WeeklyFreqWeight>>(weightFreqList, HttpStatus.OK);
    }

    //---------- GitHub Routes --------------

    //GET the weights for the selected email
    @RequestMapping(value = "/github/weight", method = RequestMethod.GET)
    public ResponseEntity<List<GitHubWeight>> getGitHubWeight(@RequestHeader(name = "email") String email,
                                                              HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = weightDao.getWeightByEmail(email);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }

    @RequestMapping(value = "/github/weight", method = RequestMethod.POST)
    public void updateGitHubData(HttpServletRequest request, HttpServletResponse response) {
        List<Team> teams = teamService.listReadAll();
        for (Team team : teams) {
            Course course = (Course) courseService.read(team.getCourse());
            gatherData.fetchData(team.getGithub_owner(), team.getGithub_repo_id(), course.getCourse(), team.getTeam_name(), team.getGithub_token());
        }
    }

    //GET the commits for the selected email
    @RequestMapping(value = "/github/commits", method = RequestMethod.GET)
    public ResponseEntity<List<CommitData>> getGitHubCommits(@RequestHeader(name = "email") String email,
                                                             HttpServletRequest request, HttpServletResponse response) {
        List<CommitData> commitList = commitDao.getCommitByEmail(email);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @RequestMapping(value = "/github/commits_course", method = RequestMethod.GET)
    public ResponseEntity<List<CommitData>> getGitHubCommitsByCourse(@RequestHeader(name = "course") String course,
                                                                     @RequestHeader(name = "weekBeginning",required = false) long weekBeginning,
                                                                     @RequestHeader(name = "weekEnding", required = false) long weekEnding,
                                                                     HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning ); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        List<CommitData> commitList = gitHubQueryDao.getCommitsByCourse(course, formattedDateBegin, formattedDateEnd);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @RequestMapping(value = "/github/commits_team", method = RequestMethod.GET)
    public ResponseEntity<List<CommitData>> getGitHubCommitsByTeam(@RequestHeader(name = "course", required = true) String course,
                                                                   @RequestHeader(name = "team", required = true) String team,
                                                                   @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                   @RequestHeader(name = "weekEnding", required = true) long weekEnding,
                                                                   HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfEnd.format(dateEnd);
        List<CommitData> commitList = gitHubQueryDao.getCommitsByTeam(course, team, formattedDateBegin, formattedDateEnd);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @RequestMapping(value = "/github/commits_student", method = RequestMethod.GET)
    public ResponseEntity<List<CommitData>> getGitHubCommitsByStudent(@RequestHeader(name = "course", required = true) String course,
                                                                      @RequestHeader(name = "team", required = true) String team,
                                                                      @RequestHeader(name = "email", required = true) String email,
                                                                      @RequestHeader(name = "weekBeginning", required = true) long weekBeginning,
                                                                      @RequestHeader(name = "weekEnding", required = true) long weekEnding,
                                                                      HttpServletRequest request, HttpServletResponse response) {
        Date dateBegin = new Date(weekBeginning); // *1000 is to convert seconds to milliseconds
        Date dateEnd = new Date(weekEnding); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDateBegin = sdfBegin.format(dateBegin);
        String formattedDateEnd = sdfBegin.format(dateEnd);
        List<CommitData> commitList = gitHubQueryDao.getCommitsByStudent(course, team, email,formattedDateBegin, formattedDateEnd);
        return new ResponseEntity<List<CommitData>>(commitList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/github/course_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getGithubCourseIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                          HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) gitHubQueryDao.getWeeklyIntervalsByCourse(course);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/github/team_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getGithubTeamIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                        @RequestHeader(name = "team", required = true) String team,
                                                                        HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) gitHubQueryDao.getWeeklyIntervalsByTeam(course, team);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @RequestMapping(value = "/github/student_intervals", method = RequestMethod.GET)
    public ResponseEntity<List<WeeklyIntervals>> getGithubStudentIntervals(@RequestHeader(name = "course", required = true) String course,
                                                                           @RequestHeader(name = "team", required = true) String team,
                                                                           @RequestHeader(name = "email", required = true) String email,
                                                                           String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) gitHubQueryDao.getWeeklyIntervalsByStudent(course, team, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @RequestMapping(value = "/github/weights_course", method = RequestMethod.GET)
    public ResponseEntity<List<GitHubWeight>> getGitHubWeightsByCourse(@RequestHeader(name = "course") String course,
                                                                       HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = gitHubWeightQueryDao.getWeightsByCourse(course);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @RequestMapping(value = "/github/weights_team", method = RequestMethod.GET)
    public ResponseEntity<List<GitHubWeight>> getGitHubWeightsByTeam(@RequestHeader(name = "course", required = true) String course,
                                                                     @RequestHeader(name = "team", required = true) String team,
                                                                     HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = gitHubWeightQueryDao.getWeightsByTeam(course, team);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }

    //GET the commits for the selected email
    @RequestMapping(value = "/github/weights_student", method = RequestMethod.GET)
    public ResponseEntity<List<GitHubWeight>> getGitHubWeightsByStudent(@RequestHeader(name = "course", required = true) String course,
                                                                        @RequestHeader(name = "team", required = true) String team,
                                                                        @RequestHeader(name = "email", required = true) String email,
                                                                        HttpServletRequest request, HttpServletResponse response) {
        List<GitHubWeight> weightList = gitHubWeightQueryDao.getWeightsByStudent(course, team, email);
        return new ResponseEntity<List<GitHubWeight>>(weightList, HttpStatus.OK);
    }
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/studentProfileDelTeam", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteStudentFromGUITeam(@RequestHeader(name = "course", required = true) String course,
                                        @RequestHeader(name = "team", required = true) String team,
                                        @RequestHeader(name = "email", required = true) String email,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            User user = userRepo.findByEmail(email);
            Object object = studentService.find(email, team, course);
            Student student = new Student();
            if (object.getClass() == Student.class) {
                student = (Student) object;
                usersService.deleteUser(user);
                taskTotalService.deleteTaskTotalsByStudent(student);
                gitHubWeightQueryDao.deleteWeightsByStudent(student);
                gitHubQueryDao.deleteCommitsByStudent(student);
                slackMessageTotalsService.deleteMessagesByStudent(student);
                memberDao.deleteMembersByStudent(student);
                response.setStatus(HttpServletResponse.SC_OK);
                return studentService.delete(student);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/studentProfileDelCourse", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> void deleteStudentFromGUICourse(@RequestHeader(name = "course", required = true) String course,
                                        @RequestHeader(name = "email", required = true) String email,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            List<Student> students = studentService.listReadStudent(course, email);
            if (!students.isEmpty()) {
                {
                    for (Student student : students) {
                        taskTotalService.deleteTaskTotalsByStudent(student);
                        gitHubWeightQueryDao.deleteWeightsByStudent(student);
                        gitHubQueryDao.deleteCommitsByStudent(student);
                        slackMessageTotalsService.deleteMessagesByStudent(student);
                        memberDao.deleteMembersByStudent(student);
                        studentService.delete(student);
                    }
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/adminProfileDelete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteAdminFromGUI(@RequestHeader(name = "course", required = true) String course,
                                  @RequestHeader(name = "email", required = true) String email,
                                  HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            Object object = adminService.find(email, course);
            Admin admin = new Admin();
            if (object.getClass() == Admin.class) {
                admin = (Admin) object;
                response.setStatus(HttpServletResponse.SC_OK);
                return adminService.delete(admin);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/userProfileDelete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteUserFromGUI(@RequestHeader(name = "email", required = true) String email,
                                 HttpServletRequest request, HttpServletResponse response) {
        if (email != null) {
            User user = userRepo.findByEmail(email);
            response.setStatus(HttpServletResponse.SC_OK);
            return usersService.deleteUser(user);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/studentDisable", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object disableStudentFromGUI(@RequestHeader(name = "course", required = true) String course,
                                     @RequestHeader(name = "team", required = true) String team,
                                     @RequestHeader(name = "email", required = true) String email,
                                     HttpServletRequest request, HttpServletResponse response) {
        Object object = studentService.find(email, team, course);
        Student student = new Student();
        if (object.getClass() == Student.class) {
            student = (Student) object;
            student.setDisabled();
            studentRepo.save(student);
        }
        return object;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/studentEnable", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object enableStudentFromGUI(@RequestHeader(name = "course", required = true) String course,
                                     @RequestHeader(name = "team", required = true) String team,
                                     @RequestHeader(name = "email", required = true) String email,
                                     HttpServletRequest request, HttpServletResponse response) {
        Object object = studentService.find(email, team, course);
        Student student = new Student();
        if (object.getClass() == Student.class) {
            student = (Student) object;
            student.setEnabled();
            studentRepo.save(student);
        }
        return object;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/userDisable", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object disableUserFromGUI(@RequestHeader(name = "email", required = true) String email,
                                     HttpServletRequest request, HttpServletResponse response) {
        Object object = userRepo.findByEmail(email);
        User user = new User();
        if (object.getClass() == User.class) {
            user = (User) object;
            user.setDisabled();
            userRepo.save(user);
        }
        return object;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/userEnable", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object enableUserFromGUI(@RequestHeader(name = "email", required = true) String email,
                                    HttpServletRequest request, HttpServletResponse response) {
        Object object = userRepo.findByEmail(email);
        User user = new User();
        if (object.getClass() == User.class) {
            user = (User) object;
            user.setEnabled();
            userRepo.save(user);
        }
        return object;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/ag_url", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public
    @ResponseBody
    String getAutograderURL(HttpServletRequest request, HttpServletResponse response) {

        Properties properties = new Properties();
        String url = "";

        BufferedReader reader = null;
        try {
            InputStream in = getClass().getResourceAsStream("/autograder.properties");
            reader = new BufferedReader(new InputStreamReader(in));
            properties.load(reader);
            url = properties.getProperty("ag_url");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return url;
    }
}