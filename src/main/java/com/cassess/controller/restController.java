package com.cassess.controller;

import com.cassess.entity.Course;
import com.cassess.entity.Student;
import com.cassess.service.ICourseService;
import com.cassess.service.IStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class restController {


    @Autowired
    private ICourseService courseService;

    @Autowired
    private IStudentsService studentsService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(value = "/course?course={course}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object getCourse(@PathVariable("course") String course, HttpServletRequest request, HttpServletResponse response) {

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return courseService.read(course);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course?course={course}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteCourse(@PathVariable("course") String course, HttpServletRequest request, HttpServletResponse response) {

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
    @RequestMapping(value = "/course_list", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Object> updateCourses(@RequestBody ArrayList<Course> courses, HttpServletRequest request, HttpServletResponse response) {

        if (courses == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return courseService.listUpdate(courses);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/course_list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Object> newCourses(@RequestBody ArrayList<Course> courses, HttpServletRequest request, HttpServletResponse response) {

        if (courses == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return courseService.listCreate(courses);
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
    @RequestMapping(value = "/student", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(value = "/student?email={email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object getStudent(@PathVariable("email") String email, HttpServletRequest request, HttpServletResponse response) {

        if (email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.find(email);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student?email={email}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteStudent(@PathVariable("email") String email, HttpServletRequest request, HttpServletResponse response) {

        if (email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.delete(email);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Object> updateStudentList(@RequestBody List<Student> students, HttpServletRequest request, HttpServletResponse response) {

        if (students == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listUpdate(students);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Object> newStudent(@RequestBody List<Student> students, HttpServletRequest request, HttpServletResponse response) {

        if (students == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listCreate(students);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Student> getAllStudents(HttpServletRequest request, HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_OK);
        return studentsService.listReadAll();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list?course={course}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Student> getStudentsByCourse(@PathVariable("course") String course, HttpServletRequest request, HttpServletResponse response) {

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listReadByCourse(course);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student_list?project={project_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> List<Student> getStudentsByProject(@PathVariable("project_name") String project_name, HttpServletRequest request, HttpServletResponse response) {

        if (project_name == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.listReadByProject(project_name);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student?course={course}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteStudentsByCourse(@PathVariable("course") String course, HttpServletRequest request, HttpServletResponse response) {

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.deleteByCourse(course);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/student?project={project}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    <T> Object deleteStudentsByProject(@PathVariable("project") String project, HttpServletRequest request, HttpServletResponse response) {

        if (project == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return studentsService.deleteByProject(project);
        }
    }
}

