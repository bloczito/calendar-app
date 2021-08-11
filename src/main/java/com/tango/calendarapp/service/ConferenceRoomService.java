package com.tango.calendarapp.service;

import com.tango.calendarapp.dto.CreateConferenceRoomRequest;
import com.tango.calendarapp.misc.Utils;
import com.tango.calendarapp.model.ConferenceRoom;
import com.tango.calendarapp.model.User;
import com.tango.calendarapp.repository.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConferenceRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;

    private final Utils utils;

    @Autowired
    public ConferenceRoomService(
            ConferenceRoomRepository conferenceRoomRepository,
            Utils utils
    ) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.utils = utils;
    }


    public ConferenceRoom createConferenceRoom(CreateConferenceRoomRequest request) {
        User currentUser = utils.getCurrentUser();

        ConferenceRoom newConferenceRoom = ConferenceRoom.builder()
                .name(request.getName())
                .companyId(currentUser.getCompanyId())
                .address(request.getAddress())
                .manager(currentUser)
                .build();

        return conferenceRoomRepository.save(newConferenceRoom);
    }

    public List<ConferenceRoom> getCompanyConferenceRooms() {
        return conferenceRoomRepository.getAllByCompanyId(utils.getCurrentUser().getCompanyId());
    }

    public Optional<ConferenceRoom> getById(Long id) {
        return conferenceRoomRepository.getByIdAndCompanyId(id, utils.getCurrentUser().getCompanyId());
    }

}
