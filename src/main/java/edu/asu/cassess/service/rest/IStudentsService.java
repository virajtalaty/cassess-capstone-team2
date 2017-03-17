package edu.asu.cassess.service.rest;

import java.util.List;

import org.json.JSONObject;

import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.taiga.Slugs;

public interface IStudentsService {

    <T> Object create(Student student);

    <T> Object update(Student student);

    <T> Object find(String email);

    <T> Object delete(String email);

    <T> List<Student> listReadAll();

    <T> List<Student> listReadByCourse(String course);

    <T> List<Student> listReadByProject(String project_name);

    JSONObject listUpdate(List<Student> students);

    JSONObject listCreate(List<Student> students);

    <T> Object deleteByCourse(String course);

    <T> Object deleteByProject(String project_name);

    List<Slugs> listGetSlugs(String course);
}