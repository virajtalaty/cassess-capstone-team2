package edu.asu.cassess.service.rest;

import edu.asu.cassess.model.Taiga.CourseList;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.security.User;
import org.json.JSONObject;

import java.util.List;

public interface IAdminsService {

    <T> Object create(Admin admin);

    <T> Object update(Admin admin);

    <T> Object find(String email);

    <T> Object delete(String email);

    <T> List<Admin> listReadAll();

    <T> List<Admin> listReadByCourse(String course);

    JSONObject listUpdate(List<Admin> admins);

    JSONObject listCreate(List<Admin> admins);

    <T> Object deleteByCourse(String course);

    List<CourseList> listGetCoursesForAdmin(String email);
}
