package com.cassess.controller;

import com.cassess.dao.taiga.ITaskTotalsQueryDao;
import com.cassess.entity.rest.Course;
import com.cassess.entity.rest.Student;
import com.cassess.entity.taiga.WeeklyTotals;
import com.cassess.service.rest.ICourseService;
import com.cassess.service.rest.IStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class restController {


    @Autowired
    private ICourseService courseService;

    @Autowired
    private IStudentsService studentsService;

    @Autowired
    ITaskTotalsQueryDao taskTotalService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object updateCourse(@RequestBody Course course, HttpServletRequest request, HttpServletResponse response) {

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return courseService.update(course);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object newCourse(@RequestBody Course course, HttpServletRequest request, HttpServletResponse response) {

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return courseService.create(course);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course", params = "course", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object getCourse(@RequestParam("course") String course, HttpServletRequest request, HttpServletResponse response) {

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return courseService.read(course);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course", params = "course", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteCourse(@RequestParam("course") String course, HttpServletRequest request, HttpServletResponse response) {

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            //System.out.println("Tool: " + tool);
            return courseService.delete(course);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course_list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    String updateCourses(@RequestBody ArrayList<Course> courses, HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (courses == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
                return courseService.listUpdate(courses).toString();
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course_list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    String newCourses(@RequestBody ArrayList<Course> courses, HttpServletRequest request, HttpServletResponse response) {

        if (courses == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return courseService.listCreate(courses).toString();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course_list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Course> getAllCourses(HttpServletRequest request, HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_OK);
        return courseService.listRead();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object updateStudent(@RequestBody Student student, HttpServletRequest request, HttpServletResponse response) {

        if (student == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.update(student);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object newStudent(@RequestBody Student student, HttpServletRequest request, HttpServletResponse response) {

        if (student == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.create(student);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object getStudent(@RequestParam(name = "email", required = true) String email, HttpServletRequest request, HttpServletResponse response) {

        if (email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.find(email);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    String updateStudentList(@RequestBody List<Student> students, HttpServletRequest request, HttpServletResponse response) {

        if (students == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listUpdate(students).toString();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    String newStudentList(@RequestBody List<Student> students, HttpServletRequest request, HttpServletResponse response) {

        if (students == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listCreate(students).toString();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Student> getStudents(@RequestParam(name = "course", required = false) String course,
                                  @RequestParam(name = "project", required = false) String project, HttpServletRequest request, HttpServletResponse response) {

        if(course != null){
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listReadByCourse(course);
        }else if(project != null){
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listReadByProject(project);
        }else{
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listReadAll();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object delete(@RequestParam(name = "project", required = false) String project, @RequestParam(name = "email", required = false) String email,
                                       @RequestParam(name = "course", required = false) String course, HttpServletRequest request, HttpServletResponse response) {
        if(project != null){
            System.out.println("Project: " + project);
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.deleteByProject(project);
        }else if(course != null){
            System.out.println("Course: " + course);
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.deleteByCourse(course);
        }else if(email != null){
            System.out.println("Email: " + email);
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.delete(email);
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/taigaTasks", method = RequestMethod.POST)
    ResponseEntity<List<WeeklyTotals>> getTasks(@RequestHeader(name = "name", required = true) String name, HttpServletRequest request, HttpServletResponse response) {
        System.out.print("Name is: " + name);
        List<WeeklyTotals> tasksList = (List<WeeklyTotals>) taskTotalService.getWeeklyTasks(name);
        return new ResponseEntity<List<WeeklyTotals>>(tasksList, HttpStatus.OK);
    }

}

