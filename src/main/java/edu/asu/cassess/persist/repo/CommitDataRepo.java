package edu.asu.cassess.persist.repo;

import edu.asu.cassess.persist.entity.github.CommitData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommitDataRepo extends JpaRepository<CommitData, Long> {
}
