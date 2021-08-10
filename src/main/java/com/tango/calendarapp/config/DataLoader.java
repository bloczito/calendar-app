package com.tango.calendarapp.config;

import com.tango.calendarapp.model.User;
import com.tango.calendarapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.UUID;


@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        UUID firstCompany = UUID.randomUUID();
        UUID secondCompany = UUID.randomUUID();
        UUID thirdCompany = UUID.randomUUID();


        User user1 = User.builder()
                .username("j_kowalski")
                .password(passwordEncoder.encode("123"))
                .fullName("Jan Kowalski")
                .zoneId(ZoneId.of("Europe/Warsaw"))
                .companyId(firstCompany)
                .build();

        userRepository.save(user1);

    }
}
