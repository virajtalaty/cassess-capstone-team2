package com.cassess.service;

import com.cassess.entity.Student;

import java.util.List;

public interface IStudentsService {

    <T> Object create(Student student);

    <T> Object update(Student student);

    <T> Object find(String email);

    <T> Object delete(String email);

    <T> List<Student> listReadAll();

    <T> List<Student> listReadByCourse(String project_name);

    <T> List<Student> listReadByProject(String project_name);

    <T> List<Object> listUpdate(List<Student> students);

    <T> List<Object> listCreate(List<Student> students);
}