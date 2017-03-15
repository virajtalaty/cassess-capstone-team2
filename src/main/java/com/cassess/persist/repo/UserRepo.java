package com.cassess.persist.repo;


import com.cassess.persist.entity.User;

import java.util.List;

public interface UserRepo {
    User findByLogin(String login);

    List<User> findAll();

    User findOne(Long id);

    User save(User user);
}

