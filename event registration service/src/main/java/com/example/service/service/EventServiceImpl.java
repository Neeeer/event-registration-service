package com.example.service.service;

import com.example.service.model.Attendee;
import com.example.service.model.Event;
import com.example.service.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService
{
    //event service class in charge of doing operations related to the events.
    // hashmap of <key. value> pairs, key being an event's event id and value being the event object associated to it
    private  Map<String, Event> eventdb;
    // reference to the response service
    private Response createResponse;
    // reference to the event service
    private AttendeeService attendeeService;

    //constructor
    public EventServiceImpl() {
        eventdb = new HashMap<>();
        createResponse = new Response();
    }


    // event service getter for the attendee service to get a reference and access the event database
    public EventService getEventService(){
        return this;
    }

    // attendee service setter to get a reference to the attendee database
    public void setAttendeeService(AttendeeService a){
        attendeeService = a;
    }

    // attendee service getter to get the event database
    public Map<String, Event> getEventdb(){
        return eventdb;
    }

    // get all events
    public ResponseEntity<Object> getAllEvents(){
        ArrayList<Event> eventList = new ArrayList <Event>();
        ResponseEntity<Object> response;

        if(eventdb.isEmpty()){
            response = createResponse.response("no events exist at the moment", HttpStatus.OK , null);
        }
        else{
            eventdb.forEach((k,v) -> {
                eventList.add(v);
            });
            response = createResponse.response(null, HttpStatus.OK , eventList);
        }

        return response;
    }

    // get all events that a specific user is attending
    public ResponseEntity<Object> getAllEvents(String userID){

        ArrayList<Event> eventList = new ArrayList <Event>();
        ResponseEntity<Object> response;
        // if the attendee exists get all events they are attending and create an appropriate response
        if(eventdb.isEmpty()){
            response = createResponse.response("no events exist at the moment", HttpStatus.OK , null);
        }
        else{
            if(attendeeService.getAttendeedb().containsKey(userID)){

                attendeeService.getAttendeedb().get(userID).retrieveAttendingEvents().forEach((n) -> {
                    eventList.add(eventdb.get(n));
                });
                if(eventList.isEmpty()){
                    response = createResponse.response("user is not attending any events", HttpStatus.OK , null);
                }
                else {
                    response = createResponse.response("user is attending the following events", HttpStatus.OK, eventList);
                }
            }
            // if the attendee does not exist, create an appropriate not found response
            else{
                response = createResponse.response("user does not exist", HttpStatus.NOT_FOUND , null);
            }
        }
        return response;
    }


    // create a new event with given parameters and add it to the event database hashmap
    public void createEvent(String eventID, String description, String location, LocalDateTime dateAndTime, int duration, int maxCapacity) {

        LocalDate date = dateAndTime.toLocalDate();
        LocalTime time = dateAndTime.toLocalTime();

        Event a = new Event(eventID,description, location,date,time,duration, maxCapacity);

        eventdb.put(eventID, a);

    }

    // add given user to given event
    public ResponseEntity<Object> addUserToEvent(String userID, String eventID) {
        // if specified event does exist
        Event event = eventdb.get(eventID);
        if(event != null){

            Attendee attendee = attendeeService.getAttendeedb().get(userID);
            // if specified attendee exists
            if(attendee != null){

                // if attendee is not already attending the event
                if(!attendee.containsEvent(eventID)){
                        // if the event is not full add attendee to event and to attending events in attendees object
                        if(event.getAttending() < event.getCapacity()){
                            attendee.attendEvent(eventID);
                            event.attending(userID);
                            //  success response
                            return createResponse.response("Attendee added to event: " + eventID,HttpStatus.OK, null);
                        }
                        else{
                            // event full response
                            return createResponse.response("Event: " + eventID + " is full: ",HttpStatus.CONFLICT, null);
                        }
                }
                // attendee already attending event response
                return createResponse.response("Attendee already attending event",HttpStatus.OK, null);
            }
            else{
                // attendee does not exist response
                return createResponse.response("Attendee does not exist",HttpStatus.NOT_FOUND, null);
            }
        }
        else{
            // event does not exist response
            return createResponse.response("Event does not exist",HttpStatus.NOT_FOUND, null);
        }
    }

    // remove attendee from event
    public ResponseEntity<Object> removeUserFromEvent(String userID, String eventID) {

        // if specified event does exist
        Event event = eventdb.get(eventID);
        if(event != null){
            // if specified attendee exists
            Attendee attendee = attendeeService.getAttendeedb().get(userID);
            if(attendee != null){
                // if attendee is attending the event remove it
                if(attendee.containsEvent(eventID)){
                    attendee.removeEvent(eventID);
                    event.removeFromEvent(userID);

                    // attendee removed from event successfully response
                    return createResponse.response("Attendee removed from event: " + eventID,HttpStatus.OK, null);
                }
                else {
                    // attendee not attending event response
                    return createResponse.response("Attendee is not attending specified event",HttpStatus.OK, null);
                }
            }
            else{
                // attendee does not exist response
                return createResponse.response("Attendee does not exist",HttpStatus.NOT_FOUND, null);
            }
        }
        else{
            // event does not exist response
            return createResponse.response("Event does not exist",HttpStatus.NOT_FOUND, null);
        }
    }
}
