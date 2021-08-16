package com.tango.calendarapp.service;

import com.tango.calendarapp.model.User;
import com.tango.calendarapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.getByUsername(username);
    }


    public List<User> getUsersByEmails(List<String> emails) { return  userRepository.getAllByEmailIn(emails); }


}
