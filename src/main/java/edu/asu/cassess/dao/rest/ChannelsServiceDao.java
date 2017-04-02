package edu.asu.cassess.dao.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.asu.cassess.persist.entity.rest.Channel;
import edu.asu.cassess.persist.entity.rest.RestResponse;
import edu.asu.cassess.persist.repo.rest.ChannelRepo;
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
public class ChannelsServiceDao {

    @Autowired
    private ChannelRepo channelRepo;

    protected EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public <T> Object create(Channel channel){
        if(channelRepo.findOne(channel.getId()) != null){
            return new RestResponse(channel.getId() + " already exists in database");
        }else{
            channelRepo.save(channel);
            return channel;
        }
    }

    @Transactional
    public <T> Object update(Channel channelInput) {
        Channel channel = (Channel) channelRepo.findOne(channelInput.getCourse());

        if (channel != null) {
            channelRepo.save(channelInput);
            return channelInput;
        } else {
            return new RestResponse(channelInput + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object find(String id) {
        Channel channel = (Channel) channelRepo.findOne(id);
        if (channel != null) {
            return channel;
        } else {
            return new RestResponse(id + " does not exist in database");
        }
    }

    @Transactional
    public <T> Object delete(String id) {
        Channel channel = (Channel) channelRepo.findOne(id);
        if (id != null) {
            channelRepo.delete(channel);
            return new RestResponse(id + " has been removed from the database");
        } else {
            return new RestResponse(id + " does not exist in the database");
        }
    }

    @Transactional
    public List<Channel> listRead() throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.channels", Channel.class);
        List<Channel> resultList = query.getResultList();
        return resultList;
    }

    @Transactional
    public List<Channel> listReadByTeam(String team_name) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT * FROM cassess.channels WHERE team_name = ?1", Channel.class);
        query.setParameter(1, team_name);
        List<Channel> resultList = query.getResultList();
        return resultList;
    }


    @Transactional
    public JSONObject listCreate(List<Channel> channels) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for(Channel channel:channels)
            if(channelRepo.findOne(channel.getId()) != null){
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(channel.getId() + " already exists in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else{
                channelRepo.save(channel);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(channel)));
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
    public JSONObject listUpdate(List<Channel> channels) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray successArray = new JSONArray();
        JSONArray failureArray = new JSONArray();
        for(Channel channel:channels)
            if(channelRepo.findOne(channel.getId()) == null){
                try {
                    failureArray.put(new JSONObject(ow.writeValueAsString(new RestResponse(channel.getId() + " does not exist in database"))));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else{
                channelRepo.save(channel);
                try {
                    successArray.put(new JSONObject(ow.writeValueAsString(channel)));
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
    public <T> Object deleteByTeam(String team_name) {
        Query preQuery = getEntityManager().createNativeQuery("SELECT * FROM cassess.channels WHERE team_name = ?1 LIMIT 1", Channel.class);
        preQuery.setParameter(1, team_name);
        Channel channel = (Channel) preQuery.getSingleResult();
        if(channel != null){
            Query query = getEntityManager().createNativeQuery("DELETE FROM cassess.channels WHERE team_name = ?1");
            query.setParameter(1, team_name);
            query.executeUpdate();
            return new RestResponse("All channels in team " + team_name + " have been removed from the database");
        }else{
            return new RestResponse("No channels in team " + team_name + " exist in the database");
        }
    }

}
