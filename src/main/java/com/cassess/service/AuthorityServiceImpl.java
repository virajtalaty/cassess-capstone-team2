package com.cassess.service;

import com.cassess.dao.CAssessDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cassess.persist.entity.Authority;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService{

    @Autowired
    private CAssessDAO dao;

    @Override
    public Authority findOne(Long id) {
        return dao.find(Authority.class, id);
    }

    @Override
    public List<Authority> findAll() {
        return dao.findAll(Authority.class);
    }

    @Override
    public Authority save(Authority authority) {
        return dao.save(authority);
    }
}