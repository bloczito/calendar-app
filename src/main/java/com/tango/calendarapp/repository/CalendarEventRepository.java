package com.tango.calendarapp.repository;

import com.tango.calendarapp.model.CalendarEvent;
import com.tango.calendarapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, Long>, CalendarEventRepositoryCustom {

    Optional<CalendarEvent> getByIdAndParticipantsContains(Long id, User user);
}
