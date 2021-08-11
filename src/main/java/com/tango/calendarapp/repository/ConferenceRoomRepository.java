package com.tango.calendarapp.repository;

import com.tango.calendarapp.model.ConferenceRoom;
import com.tango.calendarapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConferenceRoomRepository extends CrudRepository<ConferenceRoom, Long> {

    List<ConferenceRoom> getAllByCompanyId(UUID companyId);

    Optional<ConferenceRoom> getByIdAndCompanyId(Long id, UUID companyId);
}
