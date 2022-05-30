// An interface to the business logic, living in the service sub-package.

package com.example.service.service;

import java.time.LocalDateTime;
import java.util.Map;

import com.example.service.model.Event;
import org.springframework.http.ResponseEntity;

public interface EventService
{
    EventService getEventService();
    void setAttendeeService(AttendeeService e);
    public Map<String, Event> getEventdb();
    ResponseEntity<Object> getAllEvents();
    ResponseEntity<Object> getAllEvents(String eventID);
    void createEvent(String eventID, String description, String location, LocalDateTime date, int duration, int maxCapacity);
    ResponseEntity<Object> addUserToEvent(String userID, String eventID);
    ResponseEntity<Object> removeUserFromEvent(String userID, String eventID);

}

