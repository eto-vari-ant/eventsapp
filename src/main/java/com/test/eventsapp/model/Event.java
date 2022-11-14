package com.test.eventsapp.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated event ID")
    private long id;

    @ApiModelProperty(notes = "Description of event")
    @NotNull
    private String description;

    @ApiModelProperty(notes = "Name of event organizer")
    @NotNull
    private String organizer;

    @ApiModelProperty(notes = "Main topic of event")
    @NotNull
    private String topic;

    @ApiModelProperty(notes = "Location of event")
    @NotNull
    private String place;

    @Column(name = "event_time")
    @ApiModelProperty(notes = "Time of event")
    private LocalDateTime eventTime;

    public Event() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Event)) return false;
        if (description.equals(((Event) o).getDescription()) && organizer.equals(((Event) o).getOrganizer()) &&
                topic.equals(((Event) o).getTopic()) && place.equals(((Event) o).getPlace()) &&
                eventTime.equals(((Event) o).getEventTime())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (description != null) result *= 3 * description.hashCode();
        if (organizer != null) result *= 3 * organizer.hashCode();
        if (topic != null) result *= 3 * topic.hashCode();
        if (place != null) result *= 3 * place.hashCode();
        if (eventTime != null) result *= 3 * eventTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "organizer='" + organizer + '\'' +
                ", topic='" + topic + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
