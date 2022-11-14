package com.test.eventsapp.controller;

import com.test.eventsapp.model.Event;
import com.test.eventsapp.model.EventFilter;
import com.test.eventsapp.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@Api(value = "", tags = {"Events Controller"})
@Tag(name = "Events Controller", description = "Controller for CRUD operations with events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @ApiOperation(value = "Get the event by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Id is null"),
            @ApiResponse(code = 200, message = "This event exists"),
            @ApiResponse(code = 404, message = "This event doesnt exist"),
    }
    )
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Event> event = eventService.findOneById(id);
        return event.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Create new event", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Field/fields are not correct"),
            @ApiResponse(code = 201, message = "The event was created"),
    }
    )
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> createEvent(@RequestBody @Valid Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        eventService.addEvent(event);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete the event by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Id is null"),
            @ApiResponse(code = 202, message = "This event was deleted"),
            @ApiResponse(code = 404, message = "This event doesnt exist"),
    }
    )
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Event> event = eventService.findOneById(id);
        if (event.isPresent()) {
            eventService.deleteEventById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Update event or create new if it doesnt exist", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Field/fields are not correct"),
            @ApiResponse(code = 201, message = "The event was created"),
            @ApiResponse(code = 202, message = "The event was updated"),
    }
    )
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> updateEvent(@RequestBody @Valid Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Event> eventFromDb = eventService.findOneById(event.getId());
        if (eventFromDb.isPresent()) {
            eventService.updateEvent(event);
            return new ResponseEntity<>(eventFromDb.get(), HttpStatus.ACCEPTED);
        } else {
            eventService.addEvent(event);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @ApiOperation(value = "Get all events", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "There are no events at all"),
            @ApiResponse(code = 406, message = "Please check your request"),
            @ApiResponse(code = 400, message = "There are no events due to the filter"),
            @ApiResponse(code = 200, message = "You get the list with events"),
    }
    )
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getAllEvents(
            @RequestParam(value = "topicSorting", required = false) boolean topicSorting,
            @RequestParam(value = "organizerSorting", required = false) boolean organizerSorting,
            @RequestParam(value = "eventTimeSorting", required = false) boolean eventTimeSorting,
            @RequestParam(value = "topicFiltering", required = false) String topicFiltering,
            @RequestParam(value = "organizerFiltering", required = false) String organizerFiltering,
            @RequestParam(value = "eventTimeFiltering", required = false) String eventTimeFilteringString) {
        List<String> eventSorter = new ArrayList<>();
        if (topicSorting) eventSorter.add("topic");
        if (organizerSorting) eventSorter.add("organizer");
        if (eventTimeSorting) eventSorter.add("eventTime");

        int numberOfFilters = 0;
        EventFilter eventFilter = new EventFilter();
        if (topicFiltering != null && !topicFiltering.isEmpty()) {
            eventFilter.setTopic(topicFiltering);
            numberOfFilters++;
        }
        if (organizerFiltering != null && !organizerFiltering.isEmpty()) {
            eventFilter.setOrganizer(organizerFiltering);
            numberOfFilters++;
        }
        if (eventTimeFilteringString != null) {
            eventFilter.setEventTime(LocalDateTime.parse(eventTimeFilteringString));
            numberOfFilters++;
        }
        List<Event> events;
        if (eventSorter.size() == 0 && numberOfFilters == 0) {
            events = eventService.findAll();
            if (events.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else return new ResponseEntity<>(events, HttpStatus.OK);
        } else if (eventSorter.size() != 0 && numberOfFilters == 0) {
            events = eventService.findAllSorted(eventSorter);
            if (events.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else return new ResponseEntity<>(events, HttpStatus.OK);
        } else if (eventSorter.size() != 0) {
            events = eventService.findAllSorted(eventSorter);
            if (events.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                List<Event> eventsFiltered = eventService.findAllFiltered(eventFilter, events);
                if (eventsFiltered.size() == 0) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else return new ResponseEntity<>(eventsFiltered, HttpStatus.OK);
            }
        } else {
            events = eventService.findAll();
            if (events.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                List<Event> eventsFiltered = eventService.findAllFiltered(eventFilter, events);
                if (eventsFiltered.size() == 0) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else return new ResponseEntity<>(eventsFiltered, HttpStatus.OK);
            }
        }
    }
}
