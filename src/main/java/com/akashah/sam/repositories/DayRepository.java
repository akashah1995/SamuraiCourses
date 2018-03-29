package com.akashah.sam.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akashah.sam.models.Day;

@Repository
public interface DayRepository extends CrudRepository<Day, Long> {
	Day findOneById(long id);
}
