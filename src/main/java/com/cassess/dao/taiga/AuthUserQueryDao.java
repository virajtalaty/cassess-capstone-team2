package com.cassess.dao.taiga;

import com.cassess.entity.taiga.AuthUser;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AuthUserQueryDao {
    public List<AuthUser> getUsers() throws DataAccessException;

    public AuthUser getUser(String username) throws DataAccessException;
    
    public void removeDuplicateUser(Long id) throws DataAccessException;
}
