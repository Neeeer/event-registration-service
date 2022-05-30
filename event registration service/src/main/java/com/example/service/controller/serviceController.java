// The REST controller that handles HTTP requests.
// Lives in sub-package controller, marked with the @RestController annotation
// for auto-configuration; the @CrossOrigin annotation enables CORS.

package com.example.service.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import static org.springframework.http.ResponseEntity.ok;

import com.example.service.service.*;

import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
public class serviceController
{
    // The ServiceController depends on the Event and Attendee Services, so it needs keep a reference to it.
    private AttendeeService as;
    private EventService es;


    // Services argument tells Spring to auto-configure a AttendeeService and EventService
    // and pass it to the constructor.
    public serviceController(AttendeeService as, EventService es) {
        this.as = as;
        this.es = es;
    }

    // handle the HTTP GET requests matched with given URI expression (list_events).
    @GetMapping("/list_events")
    public ResponseEntity<Object> listEvents(@RequestParam(required=false) String userID) {

        ResponseEntity<Object> response;
        // call get all events or get a user's attending events if a user id is provided from the event service and get an appropriate response
        if(userID == null){
            response = es.getAllEvents();
        }
        else{
            response = es.getAllEvents(userID);
            System.out.println(response);
        }
        return response;
    }

    // handle the HTTP POST requests matched with given URI expression (register/{userID}) with parameters userID and requesting parameter eventID.
    @PostMapping("/register/{userID}")
    public ResponseEntity<Object> registerForEvent(@PathVariable String userID,@RequestParam(required=true) String eventID) {

        // Add provided user to given event service via event service and get an appropriate response
        ResponseEntity<Object> response =  es.addUserToEvent(userID, eventID);

        return response;
    }

// handle the HTTP DELETE requests matched with given URI expression (cancel/{userID}) with parameters userID and requesting parameter eventID.
    @DeleteMapping ("/cancel/{userID}")
    public ResponseEntity<Object> cancelEvent(@PathVariable String userID,@RequestParam(required=true) String eventID) {

        // remove provided user from given event service via event service and get an appropriate response.
        ResponseEntity<Object> response =  es.removeUserFromEvent(userID, eventID);

        return response;
    }

// handle the HTTP GET requests matched with given URI expression (listEventAttendees) and requesting parameter eventID.
    @GetMapping ("/listEventAttendees")
    public ResponseEntity<Object> listEventAttendees(@RequestParam(required=true) String eventID) {

        // list all attendees attending a particular event from given event service via attendee service and get an appropriate response.
        ResponseEntity<Object> response =  as.listEventAttendees(eventID);

        return response;
    }

    // handle the HTTP PUT requests matched with given URI expression (validateAttendees).
    @PutMapping ("/validateAttendees")
    public ResponseEntity<Object> validateAttendees() {

        // validate all attendees via attendee service and get an appropriate response.
        ResponseEntity<Object> response =  as.validateAttendees();

        return response;
    }
}
