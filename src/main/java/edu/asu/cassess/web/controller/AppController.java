package edu.asu.cassess.web.controller;

import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.taiga.CourseList;
import edu.asu.cassess.persist.entity.taiga.Teams;
import edu.asu.cassess.persist.entity.taiga.WeeklyTotals;
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
import org.springframework.stereotype.Controller;
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
    private StudentsServiceDao studentsService;

    @Autowired
    private TaskDataService taskService;

    @ResponseBody
    @RequestMapping(value = "/taigaTasks", method = RequestMethod.POST)
    public
    ResponseEntity<List<WeeklyTotals>> getTasks(@RequestHeader(name = "name", required = true) String name, HttpServletRequest request, HttpServletResponse response) {
        System.out.print("Name is: " + name);
        List<WeeklyTotals> tasksList = (List<WeeklyTotals>) taskTotalService.getWeeklyTasks(name);
        return new ResponseEntity<List<WeeklyTotals>>(tasksList, HttpStatus.OK);
    }

    @RequestMapping(value = "/taigaCourses", method = RequestMethod.GET)
    public
    ResponseEntity<List<CourseList>> getCourses(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = (List<CourseList>) coursesService.listGetCourses();
        return new ResponseEntity<List<CourseList>>(courseList, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/taigaTeams", method = RequestMethod.GET)
    public
    ResponseEntity<List<Teams>> getTeams(@RequestHeader(name = "course", required = true) String course, HttpServletRequest request, HttpServletResponse response) {
        System.out.print("Course: " + course);
        List<Teams> teamList = (List<Teams>) studentsService.listGetProjectNames(course);
        for(Teams team:teamList){
            System.out.print("Team: " + team.getTeam());
        }
        return new ResponseEntity<List<Teams>>(teamList, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/taigaStudents", method = RequestMethod.GET)
    public
    ResponseEntity<List<Student>> getStudents(@RequestHeader(name = "team", required = true) String team, HttpServletRequest request, HttpServletResponse response) {
        System.out.print("Team: " + team);
        List<Student> studentList = (List<Student>) studentsService.listReadByProject(team);
        for(Student student:studentList){
            System.out.print("Student: " + student.getFull_name());
        }
        return new ResponseEntity<List<Student>>(studentList, HttpStatus.OK);
    }

    @RequestMapping(value = "/taiga/Update/Projects", method = RequestMethod.POST)
    public
    void updateProjects(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            System.out.print("Course: " + course.getCourse());
            projects.updateProjects(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Memberships", method = RequestMethod.POST)
    public
    void updateMemberships(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            System.out.print("Course: " + course.getCourse());
            members.updateMembership(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Tasks", method = RequestMethod.POST)
    public
    void updateTasks(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            System.out.print("Course: " + course.getCourse());
            taskService.updateTaskTotals(course.getCourse());
        }
    }
}