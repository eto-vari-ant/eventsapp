package com.test.eventsapp.dao;

import com.test.eventsapp.model.Event;
import com.test.eventsapp.model.EventFilter;

import java.util.List;
import java.util.Optional;

public interface EventDAO {
    List<Event> findAll();

    List<Event> findAllSorted(List<String> eventsSorter);

    List<Event> findAllFiltered(EventFilter eventFilter, List<Event> events);

    Optional<Event> findOneById(long id);

    void addEvent(Event event);

    void deleteEventById(long id);

    void updateEvent(Event event);
}
