package com.tango.calendarapp.dto;


import com.tango.calendarapp.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateConferenceRoomRequest {

    private String name;
    private Address address;

}
