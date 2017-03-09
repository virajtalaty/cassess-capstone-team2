package com.cassess.service;

import com.cassess.persist.entity.Authority;

import java.util.List;

public interface AuthorityService {

    Authority findOne (Long id);

    List<Authority> findAll();

    Authority save(Authority authority);
}
