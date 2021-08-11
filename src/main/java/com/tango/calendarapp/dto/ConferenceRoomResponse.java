package com.tango.calendarapp.dto;


import com.tango.calendarapp.model.Address;
import com.tango.calendarapp.model.ConferenceRoom;
import com.tango.calendarapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConferenceRoomResponse {

    private Long id;
    private String name;
    private UUID companyId;

    private Address address;

    private User manager;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class User {
        private Long id;
        private String name;
        private String email;
    }


    public static ConferenceRoomResponse of(ConferenceRoom conferenceRoom) {
        com.tango.calendarapp.model.User manager = conferenceRoom.getManager();
        return ConferenceRoomResponse.builder()
                .id(conferenceRoom.getId())
                .name(conferenceRoom.getName())
                .companyId(conferenceRoom.getCompanyId())
                .address(conferenceRoom.getAddress())
                .manager(User.builder()
                        .id(manager.getId())
                        .name(manager.getFullName())
                        .email(manager.getEmail())
                        .build()
                )
                .build();
    }


}
