package edu.asu.cassess.web.controller;

import edu.asu.cassess.dao.rest.AdminsServiceDao;
import edu.asu.cassess.model.Taiga.*;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.security.User;
import edu.asu.cassess.persist.entity.taiga.*;
import edu.asu.cassess.security.SecurityUtils;
import edu.asu.cassess.service.rest.TeamsService;
import edu.asu.cassess.service.taiga.MembersService;
import edu.asu.cassess.service.taiga.ProjectService;
import edu.asu.cassess.service.taiga.TaskDataService;
import edu.asu.cassess.dao.rest.CourseServiceDao;
import edu.asu.cassess.dao.rest.StudentsServiceDao;
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
    private CourseServiceDao coursesService;

    @Autowired
    private ProjectService projects;

    @Autowired
    private MembersService members;

    @Autowired
    private TeamsService teamsService;

    @Autowired
    private StudentsServiceDao studentsService;

    @Autowired
    private AdminsServiceDao adminsService;

    @Autowired
    private TaskDataService taskService;

    @Autowired
    private SecurityUtils securityUtils;

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
    @RequestMapping(value = "/taigaTeams", method = RequestMethod.GET)
    public
    ResponseEntity<List<TeamNames>> getTeams(@RequestHeader(name = "course", required = true) String course, HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("Course: " + course);
        List<TeamNames> teamList = (List<TeamNames>) teamsService.listGetTeamNames(course);
        //for(TeamNames team:teamList){
        //System.out.print("Team: " + team.getTeam());
        //}
        return new ResponseEntity<List<TeamNames>>(teamList, HttpStatus.OK);
    }

    //Previous Query Based method to obtain Students assigned to a particular team/project
    @ResponseBody
    @RequestMapping(value = "/taigaStudents", method = RequestMethod.GET)
    public
    ResponseEntity<List<Student>> getStudents(@RequestHeader(name = "team", required = true) String team, HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("Team: " + team);
        List<Student> studentList = (List<Student>) studentsService.listReadByTeam(team);
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
    @RequestMapping(value = "/assigned_projects", method = RequestMethod.GET)
    public List<TeamNames> getAssignedProjects(@RequestHeader(name = "email", required = true) String email,
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
                                                          @RequestHeader(name = "project", required = true) String project,
                                                          @RequestHeader(name = "student", required = true) String student,
                                                          @RequestHeader(name = "weekBeginning", required = true) String weekBeginning,
                                                          @RequestHeader(name = "weekEnding", required = true) String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByStudent(weekBeginning, weekEnding, course, project, student);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Daily task totals for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/project_tasks", method = RequestMethod.GET)
    public
    ResponseEntity<List<DailyTaskTotals>> getProjectTasks(@RequestHeader(name = "course", required = true) String course,
                                                          @RequestHeader(name = "project", required = true) String project,
                                                          @RequestHeader(name = "weekBeginning", required = true) String weekBeginning,
                                                          @RequestHeader(name = "weekEnding", required = true) String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<DailyTaskTotals> tasksList = (List<DailyTaskTotals>) taskTotalService.getDailyTasksByProject(weekBeginning, weekEnding, course, project);
        return new ResponseEntity<List<DailyTaskTotals>>(tasksList, HttpStatus.OK);
    }

    //Weekly Activity for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_activity", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyUpdateActivity>> getStudentActivity(@RequestHeader(name = "course", required = true) String course,
                                                                  @RequestHeader(name = "project", required = true) String project,
                                                                  @RequestHeader(name = "student", required = true) String student, String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyUpdateActivity> activityList = (List<WeeklyUpdateActivity>) taskTotalService.getWeeklyUpdatesByStudent(course, project, student);
        return new ResponseEntity<List<WeeklyUpdateActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Activity for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/project_activity", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyUpdateActivity>> getProjectActivity(@RequestHeader(name = "course", required = true) String course,
                                                                   @RequestHeader(name = "project", required = true) String project,
                                                                   HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyUpdateActivity> activityList = (List<WeeklyUpdateActivity>) taskTotalService.getWeeklyUpdatesByProject(course, project);
        return new ResponseEntity<List<WeeklyUpdateActivity>>(activityList, HttpStatus.OK);
    }

    //Weekly Intervals for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/project_intervals", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyIntervals>> getProjectIntervals(@RequestHeader(name = "course", required = true) String course,
                                                              @RequestHeader(name = "project", required = true) String project,
                                                                  HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByProject(course, project);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly Intervals for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_intervals", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyIntervals>> getStudentIntervals(@RequestHeader(name = "course", required = true) String course,
                                                              @RequestHeader(name = "project", required = true) String project,
                                                              @RequestHeader(name = "email", required = true) String email,
                                                              String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyIntervals> intervalList = (List<WeeklyIntervals>) taskTotalService.getWeeklyIntervalsByStudent(course, project, email);
        return new ResponseEntity<List<WeeklyIntervals>>(intervalList, HttpStatus.OK);
    }

    //Weekly weights for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_weight", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyWeight>> getStudentWeight(@RequestHeader(name = "course", required = true) String course,
                                                              @RequestHeader(name = "project", required = true) String project,
                                                              @RequestHeader(name = "email", required = true) String email,
                                                              String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyWeight> weightList = (List<WeeklyWeight>) taskTotalService.getWeeklyWeightByStudent(course, project, email);
        return new ResponseEntity<List<WeeklyWeight>>(weightList, HttpStatus.OK);
    }

    //Weekly average weights for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/project_weight", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyWeight>> getProjectWeight(@RequestHeader(name = "course", required = true) String course,
                                                        @RequestHeader(name = "project", required = true) String project,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyWeight> weightList = (List<WeeklyWeight>) taskTotalService.getWeeklyWeightByProject(course, project);
        return new ResponseEntity<List<WeeklyWeight>>(weightList, HttpStatus.OK);
    }

    //Weekly average weights for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_weight", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyWeight>> getCourseWeight(@RequestHeader(name = "course", required = true) String course,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyWeight> weightList = (List<WeeklyWeight>) taskTotalService.getWeeklyWeightByCourse(course);
        return new ResponseEntity<List<WeeklyWeight>>(weightList, HttpStatus.OK);
    }

    //Current and last week weight for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_quickweight", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyWeight>> lastTwoWeekStudentWeight(@RequestHeader(name = "course", required = true) String course,
                                                        @RequestHeader(name = "project", required = true) String project,
                                                        @RequestHeader(name = "email", required = true) String email,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyWeight> weightList = (List<WeeklyWeight>) taskTotalService.lastTwoWeekWeightsByStudent(course, project, email);
        return new ResponseEntity<List<WeeklyWeight>>(weightList, HttpStatus.OK);
    }

    //Current and last week average weight for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/project_quickweight", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyWeight>> lastTwoWeekProjectWeight(@RequestHeader(name = "course", required = true) String course,
                                                        @RequestHeader(name = "project", required = true) String project,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyWeight> weightList = (List<WeeklyWeight>) taskTotalService.lastTwoWeekWeightsByProject(course, project);
        return new ResponseEntity<List<WeeklyWeight>>(weightList, HttpStatus.OK);
    }

    //Current and last week average weight for a course
    @ResponseBody
    @RequestMapping(value = "/taiga/course_quickweight", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyWeight>> lastTwoWeekCourseWeight(@RequestHeader(name = "course", required = true) String course,
                                                       String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyWeight> weightList = (List<WeeklyWeight>) taskTotalService.lastTwoWeekWeightsByCourse(course);
        return new ResponseEntity<List<WeeklyWeight>>(weightList, HttpStatus.OK);
    }

    //Weekly task status averages for a student
    @ResponseBody
    @RequestMapping(value = "/taiga/student_average", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> getStudentAverage(@RequestHeader(name = "course", required = true) String course,
                                                        @RequestHeader(name = "project", required = true) String project,
                                                        @RequestHeader(name = "email", required = true) String email,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByStudent(course, project, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Weekly task status averages for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/project_average", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> getProjectAverage(@RequestHeader(name = "course", required = true) String course,
                                                        @RequestHeader(name = "project", required = true) String project,
                                                        String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.getWeeklyAverageByProject(course, project);
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
                                                                @RequestHeader(name = "project", required = true) String project,
                                                                @RequestHeader(name = "email", required = true) String email,
                                                                String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByStudent(course, project, email);
        return new ResponseEntity<List<WeeklyAverages>>(averageList, HttpStatus.OK);
    }

    //Current and last week task status averages for a project
    @ResponseBody
    @RequestMapping(value = "/taiga/project_quickaverage", method = RequestMethod.GET)
    public
    ResponseEntity<List<WeeklyAverages>> lastTwoWeekProjectAverage(@RequestHeader(name = "course", required = true) String course,
                                                                @RequestHeader(name = "project", required = true) String project,
                                                                String weekEnding, HttpServletRequest request, HttpServletResponse response) {
        List<WeeklyAverages> averageList = (List<WeeklyAverages>) taskTotalService.lastTwoWeekAveragesByProject(course, project);
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
}