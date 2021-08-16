package com.tango.calendarapp.misc;

import com.tango.calendarapp.model.User;
import com.tango.calendarapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class Utils {

    private final UserService userService;

    @Autowired
    public Utils(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService
                .getByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public LocalDateTime parseDateToUTC(LocalDateTime localDateTime) {
        return localDateTime
                .atZone(getCurrentUser().getZoneId())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
    }

    public LocalDateTime parseDateToUserZone(LocalDateTime localDateTime) {
        return ZonedDateTime
                .of(localDateTime, getCurrentUser().getZoneId())
                .toLocalDateTime();
    }
}
