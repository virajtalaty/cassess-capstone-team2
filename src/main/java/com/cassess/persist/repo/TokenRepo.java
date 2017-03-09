package com.cassess.persist.repo;

import com.cassess.persist.entity.Token;

public interface TokenRepo {

    Token save(Token token);

    boolean delete(String series);

    Token findOne(String presentedSeries);
}