package com.cassess.persist.repo;

import com.cassess.persist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


//public interface UserRepo extends JpaRepository<User, Long> {
public interface UserRepo {
    User findByLogin(String login);

}
