package com.tango.calendarapp.service;

import com.tango.calendarapp.dto.CreateConferenceRoomRequest;
import com.tango.calendarapp.misc.Utils;
import com.tango.calendarapp.model.Address;
import com.tango.calendarapp.model.ConferenceRoom;
import com.tango.calendarapp.model.User;
import com.tango.calendarapp.repository.ConferenceRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConferenceRoomServiceTest {
    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private Utils utils;

    private UUID companyId = UUID.randomUUID();

    private ConferenceRoomService conferenceRoomService;

    User manager = User.builder()
            .id(1L)
            .username("j_kowalski")
            .password("123")
            .fullName("Jan Kowalski")
            .email("j.kowalski@gmail.com")
            .zoneId(ZoneId.of("Europe/Warsaw"))
            .companyId(companyId)
            .build();

    @BeforeEach
    public void configure() {
        conferenceRoomService = new ConferenceRoomService(conferenceRoomRepository, utils);
    }

    @Test
    void createConferenceRoom() {
        Address address = Address.builder()
                .street("Komandosów")
                .roomNumber("13")
                .country("Kraków")
                .build();

        CreateConferenceRoomRequest conferenceRoomRequest = CreateConferenceRoomRequest.builder()
                .name("Sala konferencyjna")
                .address(address)
                .build();

        when(utils.getCurrentUser()).thenReturn(manager);

        conferenceRoomService.createConferenceRoom(conferenceRoomRequest);

        verify(conferenceRoomRepository, Mockito.times(1)).save(ArgumentMatchers.any(ConferenceRoom.class));

    }

    @Test
    void getCompanyConferenceRooms() {
        ConferenceRoom conferenceRoom = ConferenceRoom.builder()
                .id(2L)
                .name("Sala konferencyjna 1")
                .address(Address.builder()
                        .street("Komandosów")
                        .roomNumber("12")
                        .country("Kraków")
                        .build()
                )
                .manager(manager)
                .events(Collections.emptyList())
                .build();

        ConferenceRoom conferenceRoom1 = ConferenceRoom.builder()
                .id(2L)
                .name("Sala konferencyjna 2")
                .address(Address.builder()
                        .street("Komandosów")
                        .roomNumber("13")
                        .country("Kraków")
                        .build()
                )
                .manager(manager)
                .events(Collections.emptyList())
                .build();

        ConferenceRoom conferenceRoom2 = ConferenceRoom.builder()
                .id(2L)
                .name("Sala konferencyjna 3")
                .address(Address.builder()
                        .street("Komandosów")
                        .roomNumber("14")
                        .country("Kraków")
                        .build()
                )
                .manager(manager)
                .events(Collections.emptyList())
                .build();

        List<ConferenceRoom> conferenceRooms = Arrays.asList(conferenceRoom, conferenceRoom1, conferenceRoom2);

        when(utils.getCurrentUser()).thenReturn(manager);
        when(conferenceRoomRepository.getAllByCompanyId(companyId)).thenReturn(conferenceRooms);

        assertThat(conferenceRoomService.getCompanyConferenceRooms()).isEqualTo(conferenceRooms);
    }

    @Test
    void getByWrongId() {
        when(utils.getCurrentUser()).thenReturn(manager);
        when(conferenceRoomRepository.getByIdAndCompanyId(3L, companyId)).thenReturn(Optional.empty());

        Optional<ConferenceRoom> actualConferenceRoom = conferenceRoomService.getById(3L);

        assertThat(actualConferenceRoom).isNotPresent();
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
                .manager(manager)
                .events(Collections.emptyList())
                .build();

        when(utils.getCurrentUser()).thenReturn(manager);
        when(conferenceRoomRepository.getByIdAndCompanyId(2L, companyId)).thenReturn(Optional.of(conferenceRoom));

        Optional<ConferenceRoom> actualConferenceRoom = conferenceRoomService.getById(2L);

        assertThat(actualConferenceRoom).isEqualTo(Optional.of(conferenceRoom));
    }



}