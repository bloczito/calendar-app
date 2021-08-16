package com.tango.calendarapp.repository;


import com.tango.calendarapp.model.CalendarEvent;
import com.tango.calendarapp.model.ConferenceRoom;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarEventRepositoryCustom {
    List<CalendarEvent> getAllByDateLocationAndQuery(
            Optional<LocalDate> optionalLocalDate,
            Optional<ConferenceRoom> optionalConferenceRoom,
            Optional<String> optionalQuery
    );
}
