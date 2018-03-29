package com.akashah.sam.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akashah.sam.models.Course;
import com.akashah.sam.models.User;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
	Course findOneById(long id);
}
