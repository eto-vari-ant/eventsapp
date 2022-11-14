package com.test.eventsapp.service;

import com.test.eventsapp.dao.EventDAO;
import com.test.eventsapp.model.Event;
import com.test.eventsapp.model.EventFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventDAO eventDAO;

    @Autowired
    public EventServiceImpl(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public List<Event> findAll() {
        log.info("Getting all events");
        return eventDAO.findAll();
    }

    @Override
    public List<Event> findAllSorted(List<String> eventsSorter) {
        log.info("Getting sorted list of events");
        return eventDAO.findAllSorted(eventsSorter);
    }

    @Override
    public List<Event> findAllFiltered(EventFilter eventFilter, List<Event> events) {
        log.info("Getting filtered list of events");
        return eventDAO.findAllFiltered(eventFilter, events);
    }

    @Override
    public Optional<Event> findOneById(long id) {
        log.info("The request to find event with id {}", id);
        return eventDAO.findOneById(id);
    }

    @Override
    public void addEvent(Event event) {
        log.info("The request to add event with topic {}", event.getTopic());
        eventDAO.addEvent(event);
    }

    @Override
    public void deleteEventById(long id) {
        log.info("The request to delete event with id {}", id);
        eventDAO.deleteEventById(id);
    }

    @Override
    public void updateEvent(Event event) {
        log.info("The request to update event with topic {}", event.getTopic());
        eventDAO.updateEvent(event);
    }
}
