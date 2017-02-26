package com.cassess.persist.repo;

import com.cassess.persist.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token, String> {
}