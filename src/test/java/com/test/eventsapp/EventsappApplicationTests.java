package com.test.eventsapp;

import com.test.eventsapp.model.Event;
import com.test.eventsapp.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class EventsappApplicationTests {

	private final EventService eventService;


	@Autowired
	public EventsappApplicationTests(EventService eventService) {
		this.eventService = eventService;
	}

	@Test
	@Order(1)
	public void testCreate() {
		Event event = new Event();
		event.setOrganizer("Varya");
		event.setDescription("Simple Event");
		event.setTopic("Simple topic");
		event.setPlace("Simple Place");
		event.setEventTime(LocalDateTime.parse("2022-10-13T16:04:43.384"));
		eventService.addEvent(event);
		Assertions.assertNotNull(eventService.findOneById(event.getId()));
	}


	@Test
	@Order(2)
	public void testReadAll() {
		Event event = new Event();
		event.setOrganizer("Yana");
		event.setDescription("No event");
		event.setTopic("No topic");
		event.setPlace("No place");
		event.setEventTime(LocalDateTime.parse("2019-12-22T22:36:43.384"));
		eventService.addEvent(event);
		int size = eventService.findAll().size();
		Assertions.assertTrue(size>0);
	}

}
