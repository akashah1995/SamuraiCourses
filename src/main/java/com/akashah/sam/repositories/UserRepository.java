package com.akashah.sam.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akashah.sam.models.*;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String email);
}
