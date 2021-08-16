package com.tango.calendarapp.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    private User owner;

    private String agenda;

    private LocalDateTime start;
    private LocalDateTime end;

    @ManyToOne
    private ConferenceRoom conferenceRoom;

    @ManyToMany
    private Set<User> participants;

}
