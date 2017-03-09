package com.cassess.persist.repo;

import com.cassess.persist.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cassess.dao.CAssessDAO;

@Repository
public class TokenRepoImpl implements TokenRepo {

    @Autowired
    private CAssessDAO dao;

    @Override
    public Token save(Token token) {
        return dao.save(token);
    }

    @Override
    public boolean delete(String series) {
        return dao.removeById(Token.class, series);
    }

    @Override
    public Token findOne(String presentedSeries) {
        return dao.find(Token.class, presentedSeries);
    }

}