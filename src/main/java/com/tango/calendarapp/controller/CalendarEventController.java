package com.tango.calendarapp.controller;


import com.tango.calendarapp.dto.CalendarEventResponse;
import com.tango.calendarapp.dto.CreateCalendarEventRequest;
import com.tango.calendarapp.model.CalendarEvent;
import com.tango.calendarapp.service.CalendarEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/calendarEvents")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    @Autowired
    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }





    @GetMapping
    public ResponseEntity<List<CalendarEventResponse>> getAll(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> day,
            @RequestParam(name = "location_id") Optional<Long> locationId,
            @RequestParam Optional<String> query
    ) {
        return ResponseEntity.ok(calendarEventService.getAllUserEvents(day, query, locationId)
                .stream()
                .map(calendarEventService::parseToDTO)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarEventResponse> getById(@PathVariable Long id) {
        return calendarEventService
                .getById(id)
                .map(calendarEventService::parseToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateCalendarEventRequest request) {
        try {
            CalendarEvent event = calendarEventService.createEvent(request);

            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(event.getId())
                    .toUri();

            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            log.error("Creating event error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    




}
