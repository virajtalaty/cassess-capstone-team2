package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepo extends JpaRepository<Team, String> {

}
