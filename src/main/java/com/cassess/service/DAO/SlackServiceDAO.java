package com.cassess.service.DAO;

import com.cassess.model.slack.UserObject;
import org.springframework.dao.DataAccessException;

import javax.persistence.Query;
import java.util.List;

public interface SlackServiceDAO {

    public List<UserObject> getUsers() throws DataAccessException;

    public UserObject getUser(String id) throws DataAccessException;

}
