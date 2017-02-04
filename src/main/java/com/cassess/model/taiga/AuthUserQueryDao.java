package com.cassess.model.taiga;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AuthUserQueryDao {
    public List<AuthUser> getUsers() throws DataAccessException;

    public AuthUser getUser(String email) throws DataAccessException;
}
