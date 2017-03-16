package com.cassess.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cassess.dao.rest.CourseServiceDaoImpl;
import com.cassess.dao.rest.StudentsServiceDaoImpl;
import com.cassess.dao.taiga.ITaskTotalsQueryDao;
import com.cassess.entity.rest.Course;
import com.cassess.entity.rest.Student;
import com.cassess.entity.taiga.CourseList;
import com.cassess.entity.taiga.TaskTotals;
import com.cassess.entity.taiga.Teams;
import com.cassess.entity.taiga.WeeklyTotals;
import com.cassess.service.taiga.MembersService;
import com.cassess.service.taiga.ProjectService;
import com.cassess.service.taiga.TaskDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cassess.entity.slack.UserObject;
import com.cassess.service.ISlackService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AppController {

	@Autowired
	ISlackService slackService;

	@Autowired
    ITaskTotalsQueryDao taskTotalService;

    @Autowired
    CourseServiceDaoImpl coursesService;

    @Autowired
    ProjectService projects;

    @Autowired
    MembersService members;

    @Autowired
    StudentsServiceDaoImpl studentsService;

    @Autowired
    TaskDataService taskService;
	
	@RequestMapping(value = "/slack_resource", method = RequestMethod.GET)
	public ResponseEntity<List<UserObject>> getAllUsers() {
		slackService.fetchSaveUserList();
		List<UserObject> usrList = (List<UserObject>) slackService.getAllUsers();
		return new ResponseEntity<List<UserObject>>(usrList, HttpStatus.OK);
	}

    @ResponseBody
	@RequestMapping(value = "/taigaTasks", method = RequestMethod.POST)
    ResponseEntity<List<WeeklyTotals>> getTasks(@RequestHeader(name = "name", required = true) String name, HttpServletRequest request, HttpServletResponse response) {
        System.out.print("Name is: " + name);
		List<WeeklyTotals> tasksList = (List<WeeklyTotals>) taskTotalService.getWeeklyTasks(name);
		return new ResponseEntity<List<WeeklyTotals>>(tasksList, HttpStatus.OK);
	}

    @RequestMapping(value = "/taigaCourses", method = RequestMethod.GET)
    ResponseEntity<List<CourseList>> getCourses(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = (List<CourseList>) coursesService.listGetCourses();
        return new ResponseEntity<List<CourseList>>(courseList, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/taigaTeams", method = RequestMethod.GET)
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
    ResponseEntity<List<Student>> getStudents(@RequestHeader(name = "team", required = true) String team, HttpServletRequest request, HttpServletResponse response) {
        System.out.print("Team: " + team);
        List<Student> studentList = (List<Student>) studentsService.listReadByProject(team);
        for(Student student:studentList){
            System.out.print("Student: " + student.getFull_name());
        }
        return new ResponseEntity<List<Student>>(studentList, HttpStatus.OK);
    }

    @RequestMapping(value = "/taiga/Update/Projects", method = RequestMethod.POST)
    void updateProjects(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            System.out.print("Course: " + course.getCourse());
            projects.updateProjects(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Memberships", method = RequestMethod.POST)
    void updateMemberships(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            System.out.print("Course: " + course.getCourse());
            members.updateMembership(course.getCourse());
        }
    }

    @RequestMapping(value = "/taiga/Update/Tasks", method = RequestMethod.POST)
    void updateTasks(HttpServletRequest request, HttpServletResponse response) {
        List<CourseList> courseList = coursesService.listGetCourses();
        for (CourseList course : courseList) {
            System.out.print("Course: " + course.getCourse());
            taskService.updateTaskTotals(course.getCourse());
        }
    }
}
