package com.cassess.service;

import com.cassess.entity.Course;

import java.util.List;


public interface ICourseService {

    <T> Object create(Course course);

    <T> Object update(Course course);

    <T> Object read(String course);

    <T> Object delete(String course);

    <T> List<Course> listRead();

    <T> List<Object> listCreate(List<Course> courses);

    <T> List<Object> listUpdate(List<Course> courses);
}
