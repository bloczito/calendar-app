package com.tango.calendarapp.repository;

import com.tango.calendarapp.model.CalendarEvent;
import org.springframework.data.repository.CrudRepository;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, Long> {
}
