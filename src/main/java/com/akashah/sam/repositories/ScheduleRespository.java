package com.akashah.sam.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akashah.sam.models.Schedule;
import com.akashah.sam.models.User;

@Repository
public interface ScheduleRespository extends CrudRepository<Schedule, Long> {
	Schedule findOneById(long id);
}