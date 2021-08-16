package com.tango.calendarapp.dto;

import com.tango.calendarapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEventResponse {

    private Long id;

    private String name;
    private String agenda;

    private User owner;

    private LocalDateTime start;
    private LocalDateTime end;

    private ConferenceRoomResponse conferenceRoom;

    private List<User> participants;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class User {
        private Long id;
        private String name;
        private String email;

        public User(com.tango.calendarapp.model.User user) {
            this.id = user.getId();
            this.name = user.getFullName();
            this.email = user.getEmail();
        }
    }

}
