package com.cassess.persist.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cassess.persist.entity.Authority;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {

}