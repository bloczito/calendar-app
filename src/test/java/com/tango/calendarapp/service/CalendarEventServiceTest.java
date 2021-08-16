package com.tango.calendarapp.service;

import com.tango.calendarapp.dto.CreateCalendarEventRequest;
import com.tango.calendarapp.misc.Utils;
import com.tango.calendarapp.model.Address;
import com.tango.calendarapp.model.CalendarEvent;
import com.tango.calendarapp.model.ConferenceRoom;
import com.tango.calendarapp.model.User;
import com.tango.calendarapp.repository.CalendarEventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarEventServiceTest {

    @Mock
    private CalendarEventRepository calendarEventRepository;
    @Mock
    private ConferenceRoomService conferenceRoomService;
    @Mock
    private UserService userService;
    @Mock
    private Utils utils;

    private CalendarEventService calendarEventService;

    private UUID companyId;

    private User owner;
    private User user2;
    private User user3;


    @BeforeEach
    public void configure() {
        companyId = UUID.randomUUID();
        calendarEventService = new CalendarEventService(calendarEventRepository, conferenceRoomService, userService, utils);

        owner = User.builder()
                .id(1L)
                .username("j_kowalski")
                .password("123")
                .fullName("Jan Kowalski")
                .email("j.kowalski@gmail.com")
                .zoneId(ZoneId.of("Europe/Warsaw"))
                .companyId(companyId)
                .build();

        user2 = User.builder()
                .username("b_krawiec")
                .password("123")
                .fullName("Bartosz Krawiec")
                .email("b.krawiec@gmail.com")
                .zoneId(ZoneId.of("Europe/Warsaw"))
                .companyId(companyId)
                .build();

        user3 = User.builder()
                .username("m_nowak")
                .password("123")
                .fullName("Marek Nowak")
                .email("m.nowak@gmail.com")
                .zoneId(ZoneId.of("+5"))
                .companyId(companyId)
                .build();

        ReflectionTestUtils.setField(calendarEventService, "eventMaxDuration", 28800);
    }


    @Test
    void getByCorrectId() {
        ConferenceRoom conferenceRoom = ConferenceRoom.builder()
                .id(2L)
                .name("Sala konferencyjna 1")
                .address(Address.builder()
                        .street("Komandosów")
                        .roomNumber("12")
                        .country("Kraków")
                        .build()
                )
                .manager(owner)
                .events(Collections.emptyList())
                .build();

        CalendarEvent calendarEvent = CalendarEvent.builder()
                .id(1L)
                .name("Spotkanie organizacyjne")
                .owner(owner)
                .agenda("sprint")
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(2))
                .conferenceRoom(conferenceRoom)
                .participants(Sets.newSet(owner, user2, user3))
                .build();

        when(utils.getCurrentUser()).thenReturn(owner);
        when(calendarEventRepository.getByIdAndParticipantsContains(1L, owner)).thenReturn(Optional.of(calendarEvent));

        Optional<CalendarEvent> result = calendarEventService.getById(1L);

        assertThat(result).isEqualTo(Optional.of(calendarEvent));
    }

    @Test
    void getByWrongId() {
        when(utils.getCurrentUser()).thenReturn(owner);
        when(calendarEventRepository.getByIdAndParticipantsContains(2L, owner)).thenReturn(Optional.empty());

        assertThat(calendarEventService.getById(2L)).isNotPresent();
    }

    @Test
    void createEventCorrect() throws Exception {
        List<String> participantsMails = Arrays.asList("b.krawiec@gmail.com", "m.nowak@gmail.com");

        ConferenceRoom conferenceRoom = ConferenceRoom.builder()
                .id(2L)
                .name("Sala konferencyjna 1")
                .address(Address.builder()
                        .street("Komandosów")
                        .roomNumber("12")
                        .country("Kraków")
                        .build()
                )
                .manager(owner)
                .events(Collections.emptyList())
                .build();

        LocalDateTime eventStart = LocalDateTime.now().plusHours(1);
        LocalDateTime eventEnd = LocalDateTime.now().plusHours(2);

        CreateCalendarEventRequest request = CreateCalendarEventRequest.builder()
                .name("Spotkanie organizacyjne")
                .agenda("meeting")
                .start(eventStart)
                .end(eventEnd)
                .participants(participantsMails)
                .conferenceRoomId(2L)
                .build();

        when(userService.getUsersByEmails(participantsMails)).thenReturn(Arrays.asList(user2, user3));
        when(utils.getCurrentUser()).thenReturn(owner);
        when(conferenceRoomService.getById(2L)).thenReturn(Optional.of(conferenceRoom));
        when(utils.parseDateToUTC(eventStart)).thenReturn(parseDateToUTC(eventStart, owner));
        when(utils.parseDateToUTC(eventEnd)).thenReturn(parseDateToUTC(eventEnd, owner));

        calendarEventService.createEvent(request);

        verify(calendarEventRepository, Mockito.times(1)).save(ArgumentMatchers.any(CalendarEvent.class));
    }

    @Test
    void createEventLongerThan8Hours() {
        List<String> participantsMails = Arrays.asList("b.krawiec@gmail.com", "m.nowak@gmail.com");

        LocalDateTime eventStart = LocalDateTime.now().plusHours(1);
        LocalDateTime eventEnd = LocalDateTime.now().plusHours(10);

        CreateCalendarEventRequest request = CreateCalendarEventRequest.builder()
                .name("Spotkanie organizacyjne")
                .agenda("meeting")
                .start(eventStart)
                .end(eventEnd)
                .participants(participantsMails)
                .conferenceRoomId(2L)
                .build();

        when(utils.getCurrentUser()).thenReturn(owner);

        assertThatThrownBy(() -> calendarEventService.createEvent(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Event cannot be longer than 8 hours");
    }

    @Test
    void createEventThatStartInThePast() {
        List<String> participantsMails = Arrays.asList("b.krawiec@gmail.com", "m.nowak@gmail.com");

        LocalDateTime eventStart = LocalDateTime.now().minusHours(1);
        LocalDateTime eventEnd = LocalDateTime.now().plusHours(3);

        CreateCalendarEventRequest request = CreateCalendarEventRequest.builder()
                .name("Spotkanie organizacyjne")
                .agenda("meeting")
                .start(eventStart)
                .end(eventEnd)
                .participants(participantsMails)
                .conferenceRoomId(2L)
                .build();

        when(utils.getCurrentUser()).thenReturn(owner);

        assertThatThrownBy(() -> calendarEventService.createEvent(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Event cannot start in the past");
    }

    public LocalDateTime parseDateToUTC(LocalDateTime localDateTime, User user) {
        return localDateTime
                .atZone(user.getZoneId())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
    }

}