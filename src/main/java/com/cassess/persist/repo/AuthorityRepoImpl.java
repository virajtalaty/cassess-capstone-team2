package com.cassess.persist.repo;

import com.cassess.dao.CAssessDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorityRepoImpl implements AuthorityRepo {

    @Autowired
    private CAssessDAO dao;

}