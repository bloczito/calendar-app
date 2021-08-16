package com.tango.calendarapp.service;

import com.tango.calendarapp.dto.CalendarEventResponse;
import com.tango.calendarapp.dto.ConferenceRoomResponse;
import com.tango.calendarapp.dto.CreateCalendarEventRequest;
import com.tango.calendarapp.misc.Utils;
import com.tango.calendarapp.model.CalendarEvent;
import com.tango.calendarapp.model.User;
import com.tango.calendarapp.repository.CalendarEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;
    private final ConferenceRoomService conferenceRoomService;
    private final UserService userService;
    private final Utils utils;

    @Autowired
    public CalendarEventService(
            CalendarEventRepository calendarEventRepository,
            ConferenceRoomService conferenceRoomService,
            UserService userService,
            Utils utils
    ) {
        this.calendarEventRepository = calendarEventRepository;
        this.conferenceRoomService = conferenceRoomService;
        this.userService = userService;
        this.utils = utils;
    }

    @Value("${calendar.event.maxDuration}")
    private long eventMaxDuration;

    public List<CalendarEvent> getAllUserEvents() {
        return calendarEventRepository.getAllByParticipantsContains(utils.getCurrentUser());
    }

    public Optional<CalendarEvent> getById(Long id) {
        return calendarEventRepository.getAllByIdAndParticipantsContains(id, utils.getCurrentUser());
    }


    public CalendarEvent createEvent(CreateCalendarEventRequest request) throws Exception {
            validateEventRequestData(request);
            return calendarEventRepository.save(parseFromDTO(request));
    }


    private void validateEventRequestData(CreateCalendarEventRequest request) throws Exception {
        User user = utils.getCurrentUser();

        LocalDateTime startDateTime = request.getStart();
        LocalDateTime endDateTime = request.getEnd();

        if (request.getName() == null || request.getName().isEmpty())
            throw new IllegalArgumentException("Name cannot be null or blank");

        if (startDateTime == null)
            throw new IllegalArgumentException("Start date cannot be null");

        if (endDateTime == null)
            throw new IllegalArgumentException("End date cannot be null");

        LocalDateTime utcStartDateTime = startDateTime
                .atZone(user.getZoneId())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        LocalDateTime utcEndDateTime = endDateTime
                .atZone(user.getZoneId())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        if (utcStartDateTime.isBefore(LocalDateTime.now(ZoneOffset.UTC)))
            throw new IllegalArgumentException("Event cannot start in the past");

        if (utcEndDateTime.isBefore(utcStartDateTime))
            throw new IllegalArgumentException("Event cannot end before start");

        if (ChronoUnit.SECONDS.between(utcStartDateTime, utcEndDateTime) > eventMaxDuration) {
            throw new IllegalArgumentException("Event cannot be longer than 8 hours");
        }
    }


    private CalendarEvent parseFromDTO(CreateCalendarEventRequest request) {
        CalendarEvent calendarEvent = CalendarEvent.builder()
                .name(request.getName())
                .owner(utils.getCurrentUser())
                .agenda(request.getAgenda())
                .start(utils.parseDateToUTC(request.getStart()))
                .end(utils.parseDateToUTC(request.getEnd()))
                .participants(Stream.concat(userService.getUsersByEmails(request.getParticipants()).stream(), Stream.of(utils.getCurrentUser())).collect(Collectors.toSet()))
                .build();

        conferenceRoomService
                .getById(request.getConferenceRoomId())
                .ifPresent(calendarEvent::setConferenceRoom);

        return calendarEvent;
    }

    public CalendarEventResponse parseToDTO(CalendarEvent calendarEvent) {
        User owner = calendarEvent.getOwner();
        return CalendarEventResponse.builder()
                .id(calendarEvent.getId())
                .name(calendarEvent.getName())
                .agenda(calendarEvent.getAgenda())
                .owner(CalendarEventResponse.User.builder()
                        .id(owner.getId())
                        .name(owner.getFullName())
                        .email(owner.getEmail())
                        .build()
                )
                .start(utils.parseDateToUserZone(calendarEvent.getStart()))
                .end(utils.parseDateToUserZone(calendarEvent.getEnd()))
                .conferenceRoom(calendarEvent.getConferenceRoom() != null ? ConferenceRoomResponse.of(calendarEvent.getConferenceRoom()) : null)
                .participants(calendarEvent.getParticipants()
                        .stream()
                        .map(CalendarEventResponse.User::new)
                        .collect(Collectors.toList())
                )
                .build();
    }


}
