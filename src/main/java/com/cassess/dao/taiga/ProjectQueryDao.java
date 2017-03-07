package com.cassess.dao.taiga;

import com.cassess.entity.taiga.Project;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ProjectQueryDao {

    public List<Project> getProjects() throws DataAccessException;

    public Project getProject() throws DataAccessException;

}
