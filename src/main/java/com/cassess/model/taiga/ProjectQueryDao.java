package com.cassess.model.taiga;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProjectQueryDao {

    public List<Project> getProjects() throws DataAccessException;

    public Project getProject(String username) throws DataAccessException;

}
