package com.tango.calendarapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConferenceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private UUID companyId;

    @Embedded
    private Address address;

    @ManyToOne
    private User manager;

    @OneToMany(mappedBy = "conferenceRoom")
    private List<CalendarEvent> events;

}
