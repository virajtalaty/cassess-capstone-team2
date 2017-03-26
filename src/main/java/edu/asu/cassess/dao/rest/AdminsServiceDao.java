package edu.asu.cassess.dao.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.persist.entity.rest.Admin;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.repo.rest.AdminsRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Component
@Transactional
public class AdminsServiceDao {

    @Autowired
    private AdminsRepo adminDao;

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public <T> Object create(Admin admin) {
        //System.out.println("Got into create");
        if(adminDao.findOne(admin.getEmail()) != null){
            return new RestResponse(admin.getEmail() + " already exists in database");
        }else{
            adminDao.save(admin);
            return admin;
        }
    }

    @Transactional
    public <T> Object update(Admin admin) {
        if(adminDao.findOne(admin.getEmail()) != null){
            adminDao.save(admin);
            return admin;
        }else{
            return new RestResponse(admin.getEmail() + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String email) {
        Admin admin = adminDao.findOne(email);
        if(admin != null){
            return admin;
        }else{
            return new RestResponse(email + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String email) {
        Admin admin = adminDao.findOne(email);
        if(admin != null){
            adminDao.delete(admin);
            return new RestResponse(email + " has been removed from the database");
        }else{
            return new RestResponse(email + " does not exist in the database");
        }
    }

    @Transactional
    public List<Admin> listReadAll() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins", Admin.class);
        List<Admin> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Admin> listReadByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.admins WHERE course = ?1", Admin.class);
        query.setParameter(1, course);
        List<Admin> resultList = query.getResultList();
        return resultList;
    }



    @Transactional
    public JSONObject listCreate(List<Admin> admins) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for(Admin admin: admins)
            if(adminDao.findOne(admin.getEmail()) != null){
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(admin.getEmail() + " already exists in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else{
                adminDao.save(admin);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(admin)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("success", successArray);
        returnJSON.put("failure", failureArray);
        return returnJSON;
    }

    @Transactional
    public JSONObject listUpdate(List<Admin> admins) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for (Admin admin : admins) {
            if (adminDao.findOne(admin.getEmail()) == null) {
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(admin.getEmail() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                adminDao.save(admin);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(admin)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("success", successArray);
        returnJSON.put("failure", failureArray);
        return returnJSON;
    }

    @Transactional
    public <T> Object deleteByCourse(String course) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.admins WHERE course = ?1 LIMIT 1", Admin.class);
        preQuery.setParameter(1, course);
        Admin admin = (Admin) preQuery.getSingleResult();
        if(admin != null){
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.admins WHERE course = ?1");
            query.setParameter(1, course);
            query.executeUpdate();
            return new RestResponse("All admin in course " + course + " have been removed from the database");
        }else{
            return new RestResponse("No admin in course " + course + " exist in the database");
        }
    }

}
