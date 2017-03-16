package edu.asu.cassess.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.asu.cassess.persist.entity.Token;

public interface TokenRepo extends JpaRepository<Token, String> {
}
