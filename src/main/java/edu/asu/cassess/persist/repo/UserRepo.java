package edu.asu.cassess.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.asu.cassess.persist.entity.security.User;


public interface UserRepo extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
