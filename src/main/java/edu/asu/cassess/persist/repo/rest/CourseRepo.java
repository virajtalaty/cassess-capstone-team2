package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, String> {

}
