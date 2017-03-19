package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, String> {

}
