package edu.asu.cassess.service.rest;

import edu.asu.cassess.dao.rest.AdminsServiceDao;
import edu.asu.cassess.persist.entity.rest.Admin;
import org.json.JSONObject;

import javax.ejb.EJB;
import java.util.List;

public class AdminsService implements IAdminsService {

    @EJB
    private AdminsServiceDao adminsDao;

    @Override
    public <T> Object create(Admin admin) {
        return adminsDao.create(admin);
    }

    @Override
    public <T> Object update(Admin admin) {
        return adminsDao.update(admin);
    }

    @Override
    public <T> Object find(String email) {
        return adminsDao.find(email);
    }

    @Override
    public <T> Object delete(String email) {
        return adminsDao.delete(email);
    }

    @Override
    public <T> List<Admin> listReadAll() {
        return adminsDao.listReadAll();
    }

    @Override
    public <T> List<Admin> listReadByCourse(String course) {
        return adminsDao.listReadByCourse(course);
    }

    @Override
    public JSONObject listUpdate(List<Admin> admins) {
        return adminsDao.listUpdate(admins);
    }

    @Override
    public JSONObject listCreate(List<Admin> admins) {
        return adminsDao.listCreate(admins);
    }

    @Override
    public <T> Object deleteByCourse(String course) {
        return adminsDao.deleteByCourse(course);
    }
}
