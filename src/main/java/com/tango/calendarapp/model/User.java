package com.tango.calendarapp.model;


import lombok.*;

import javax.persistence.*;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String fullName;

    @Column(unique = true)
    private String email;

    private UUID companyId;

    private ZoneId zoneId;

    @OneToMany(mappedBy = "manager")
    private Set<ConferenceRoom> conferenceRooms;

    @OneToMany(mappedBy = "owner")
    private Set<CalendarEvent> ownedCalendarEvents;

    @ManyToMany
    private Set<CalendarEvent> calendarEvents;
}
