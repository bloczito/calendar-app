package com.tango.calendarapp.repository;

import com.tango.calendarapp.model.CalendarEvent;
import com.tango.calendarapp.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, Long> {



    List<CalendarEvent> getAllByParticipantsContains(User user);

    Optional<CalendarEvent> getAllByIdAndParticipantsContains(Long id, User user);
}
