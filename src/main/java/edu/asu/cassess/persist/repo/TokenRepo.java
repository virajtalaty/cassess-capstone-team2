package edu.asu.cassess.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.asu.cassess.persist.entity.security.Token;

public interface TokenRepo extends JpaRepository<Token, String> {
}
