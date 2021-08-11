package com.tango.calendarapp.config;

import com.tango.calendarapp.model.User;
import com.tango.calendarapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Arrays;
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


        //First company users
        User user1 = User.builder()
                .username("j_kowalski")
                .password(passwordEncoder.encode("123"))
                .fullName("Jan Kowalski")
                .email("j.kowalski@gmail.com")
                .zoneId(ZoneId.of("Europe/Warsaw"))
                .companyId(firstCompany)
                .build();

        User user2 = User.builder()
                .username("b_krawiec")
                .password(passwordEncoder.encode("123"))
                .fullName("Bartosz Krawiec")
                .email("b.krawiec@gmail.com")
                .zoneId(ZoneId.of("Europe/Warsaw"))
                .companyId(firstCompany)
                .build();

        User user3 = User.builder()
                .username("m_nowak")
                .password(passwordEncoder.encode("123"))
                .fullName("Marek Nowak")
                .email("m.nowak@gmail.com")
                .zoneId(ZoneId.of("+5"))
                .companyId(firstCompany)
                .build();


        //Second company users
        User user4 = User.builder()
                .username("p_szarek")
                .password(passwordEncoder.encode("123"))
                .fullName("Piotr Szarek")
                .email("p.szarek@gmail.com")
                .zoneId(ZoneId.of("-3"))
                .companyId(secondCompany)
                .build();

        User user5 = User.builder()
                .username("z_wojcik")
                .password(passwordEncoder.encode("123"))
                .fullName("Zygmunt Wójcik")
                .email("z.wojcik@gmail.com")
                .zoneId(ZoneId.of("-3"))
                .companyId(secondCompany)
                .build();

        User user6 = User.builder()
                .username("r_lewandowski")
                .password(passwordEncoder.encode("123"))
                .fullName("Robert Lewandowski")
                .email("r.lewandowski@gmail.com")
                .zoneId(ZoneId.of("-3"))
                .companyId(secondCompany)
                .build();


        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6));


    }
}
