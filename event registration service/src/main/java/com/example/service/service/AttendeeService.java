// An interface to the business logic, living in the service sub-package.

package com.example.service.service;
import java.util.Map;
import com.example.service.model.Attendee;
import org.springframework.http.ResponseEntity;
public interface AttendeeService
{
    AttendeeService getAttendeeService();
    void setEventService(EventService e);
    ResponseEntity<Object> listEventAttendees(String eventID);
    public Map<String, Attendee> getAttendeedb();
    String createAttendee();
    public ResponseEntity<Object> validateAttendees();
}

