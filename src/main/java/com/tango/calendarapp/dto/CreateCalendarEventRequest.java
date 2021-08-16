package com.tango.calendarapp.dto;

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
public class CreateCalendarEventRequest {

    private String name;
    private String agenda;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long conferenceRoomId;
    private List<String> participants;

}
