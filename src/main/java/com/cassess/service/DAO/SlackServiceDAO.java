package com.cassess.service.DAO;

import org.springframework.dao.DataAccessException;

import com.cassess.entity.slack.UserObject;

import javax.persistence.Query;
import java.util.List;

public interface SlackServiceDAO {

    public List<UserObject> getUsers() throws DataAccessException;

    public UserObject getUser(String id) throws DataAccessException;

}
