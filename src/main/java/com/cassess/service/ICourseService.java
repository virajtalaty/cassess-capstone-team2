package com.cassess.service;

import com.cassess.entity.Course;
import org.json.JSONObject;

import java.util.List;


public interface ICourseService {

    <T> Object create(Course course);

    <T> Object update(Course course);

    <T> Object read(String course);

    <T> Object delete(String course);

    <T> List<Course> listRead();

    JSONObject listCreate(List<Course> courses);

    JSONObject listUpdate(List<Course> courses);
}
