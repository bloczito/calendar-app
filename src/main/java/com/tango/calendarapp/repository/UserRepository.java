package com.tango.calendarapp.repository;

import com.tango.calendarapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> getByUsername(String username);
}
