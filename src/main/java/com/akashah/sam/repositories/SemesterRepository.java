package com.akashah.sam.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akashah.sam.models.Semester;


@Repository
public interface SemesterRepository extends CrudRepository<Semester, Long> {

}
