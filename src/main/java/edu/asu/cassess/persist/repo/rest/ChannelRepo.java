package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepo extends JpaRepository<Channel, String> {
}
