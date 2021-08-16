package com.tango.calendarapp.controller;

import com.tango.calendarapp.dto.ConferenceRoomResponse;
import com.tango.calendarapp.dto.CreateConferenceRoomRequest;
import com.tango.calendarapp.model.ConferenceRoom;
import com.tango.calendarapp.service.ConferenceRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conferenceRooms")
public class ConferenceRoomController {

    private final ConferenceRoomService conferenceRoomService;

    @Autowired
    public ConferenceRoomController(ConferenceRoomService conferenceRoomService) {
        this.conferenceRoomService = conferenceRoomService;
    }



    @GetMapping
    public ResponseEntity<List<ConferenceRoomResponse>> getAll() {
        return ResponseEntity.ok(conferenceRoomService
                .getCompanyConferenceRooms()
                .stream()
                .map(ConferenceRoomResponse::of)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConferenceRoomResponse> getById(@PathVariable Long id) {
        return conferenceRoomService
                .getById(id)
                .map(ConferenceRoomResponse::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<URI> createConferenceRoom(@RequestBody CreateConferenceRoomRequest request) {

        ConferenceRoom createdConferenceRoom = conferenceRoomService.createConferenceRoom(request);

        URI uriLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdConferenceRoom.getId())
                .toUri();

        return ResponseEntity.created(uriLocation).build();
    }
}
