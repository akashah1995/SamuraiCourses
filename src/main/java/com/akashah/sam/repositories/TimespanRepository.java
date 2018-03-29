package com.akashah.sam.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akashah.sam.models.Timespan;

@Repository
public interface TimespanRepository extends CrudRepository<Timespan, Long> {
	Timespan findOneById(long id);
}