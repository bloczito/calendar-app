package com.tango.calendarapp.service;

import com.tango.calendarapp.repository.CalendarEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;

    @Autowired
    public CalendarEventService(CalendarEventRepository calendarEventRepository) {
        this.calendarEventRepository = calendarEventRepository;
    }
}
