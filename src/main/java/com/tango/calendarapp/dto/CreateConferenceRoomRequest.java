package com.tango.calendarapp.dto;


import com.tango.calendarapp.model.Address;
import lombok.Data;

@Data
public class CreateConferenceRoomRequest {

    private String name;
    private Address address;

}
