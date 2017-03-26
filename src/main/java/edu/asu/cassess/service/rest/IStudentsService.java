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

    <T> List<Student> listReadByTeam(String team_name);

    JSONObject listUpdate(List<Student> students);

    JSONObject listCreate(List<Student> students);

    <T> Object deleteByTeam(String team_name);


}