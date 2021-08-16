package com.tango.calendarapp.repository;

import com.tango.calendarapp.misc.Utils;
import com.tango.calendarapp.model.CalendarEvent;
import com.tango.calendarapp.model.ConferenceRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CalendarEventRepositoryCustomImpl implements CalendarEventRepositoryCustom{

    private final EntityManager entityManager;
    private final Utils utils;

    @Autowired
    public CalendarEventRepositoryCustomImpl(EntityManager entityManager, Utils utils) {
        this.entityManager = entityManager;
        this.utils = utils;
    }


    @Override
    public List<CalendarEvent> getAllByDateLocationAndQuery(Optional<LocalDate> optionalDate, Optional<ConferenceRoom> optionalConferenceRoom, Optional<String> optionalQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CalendarEvent> criteriaQuery = criteriaBuilder.createQuery(CalendarEvent.class);

        Root<CalendarEvent> event = criteriaQuery.from(CalendarEvent.class);
        ArrayList<Predicate> predicates = new ArrayList<>();


        predicates.add(criteriaBuilder.isMember(utils.getCurrentUser(), event.get("participants")));

        optionalDate.ifPresent(eventDate -> predicates
                .add(criteriaBuilder.between(
                        event.get("start"),
                        criteriaBuilder.literal(eventDate.atStartOfDay()),
                        criteriaBuilder.literal(eventDate.atTime(LocalTime.MAX)))
                ));

        optionalConferenceRoom.ifPresent(conferenceRoom -> predicates
                .add(criteriaBuilder.equal(
                        event.get("conferenceRoom"),
                        criteriaBuilder.literal(conferenceRoom))
                ));

        optionalQuery.ifPresent(nameAgendaQuery -> {
            Predicate namePredicate = criteriaBuilder.like(event.get("name"), "%" + nameAgendaQuery + "%");
            Predicate agendaPredicate = criteriaBuilder.like(event.get("agenda"), "%" + nameAgendaQuery + "%");

            predicates.add(criteriaBuilder.or(namePredicate, agendaPredicate));
        });

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
