package com.tango.calendarapp.model;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String roomNumber;
}
