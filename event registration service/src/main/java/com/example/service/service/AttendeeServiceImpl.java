// Implementation of the business logic, living in the service sub-package.
// Discoverable for auto-configuration, thanks to the @Component annotation.

package com.example.service.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.service.response.Response;
import com.google.gson.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.service.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AttendeeServiceImpl implements AttendeeService
{
    //Attendee service class in charge of doing operations related to Attendees.
    // hashmap of <key. value> pairs, key being an attendee's user id and value being the attendee object associated to it
    private Map<String, Attendee> attendeedb;
    // reference to the response class
    private Response createResponse;
    // reference to the event service
    private EventService eventService;

    //constructor
    public AttendeeServiceImpl() {
         attendeedb = new HashMap<>();
         createResponse = new Response();
    }

    // attendee service getter for the event service to get access to the attendee database
    public AttendeeService getAttendeeService(){
        return this;
    }
    // event service setter to get a reference to the event database
    public void setEventService(EventService e){
        eventService = e;
    }
    // event service getter to get the event database
    public Map<String, Attendee> getAttendeedb(){
        return attendeedb;
    }

    // get provided event from the event service and return all attendees attending it
    public ResponseEntity<Object> listEventAttendees(String eventID){
        ResponseEntity<Object> response = null;
        ArrayList <String> attendeeIDList;
        Event event = eventService.getEventdb().get(eventID);
        if(event == null){
            response = createResponse.response("specified event does not exist", HttpStatus.NOT_FOUND, null);
        }
        else{

            attendeeIDList = event.retrieveAttendees();
            ArrayList <Attendee> attendeeList = new ArrayList<>();

            // if event attendee list is empty return appropriate response, else return ok status and list

            if(attendeeIDList.isEmpty()){
                response = createResponse.response("no one is attending the specified event", HttpStatus.OK, null);
            }
            else{
                // for every attendee in mapping store attendee object in a list
                attendeeIDList.forEach((n) -> {
                    attendeeList.add(attendeedb.get(n));
                });
                response = createResponse.response(null,HttpStatus.OK , attendeeList);
            }
        }
        return response;
    }

    // create an attendee object and store it
    public String createAttendee(){

        // from given url below, retrieve information about a random user
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("https://pmaier.eu.pythonanywhere.com/random-user" , String.class);
        // unsure of data structure upon get request of random user, using temporal hard coded numbers to get substring of JSON user object
        String resultObject = result.substring(158,result.length() -2);

        // turn string into a JSON object
        Gson gson = new Gson();
        JsonElement element = gson.fromJson (resultObject, JsonElement.class);
        JsonObject jsonObj = element.getAsJsonObject();


        JsonArray jsonInterests;

        ArrayList<String> interests = new ArrayList();

        // get user id, name and interests of the random user by retrieving it from the JSON object
        String uid = jsonObj.get("uid").getAsString();
        String name = jsonObj.get("name").getAsString();
        jsonInterests = jsonObj.get("interests").getAsJsonArray();
        // print user id to console to be able to use it to carry out queries
        System.out.println(uid);

        //store all user interests from JSON array to regular array
        for (JsonElement i : jsonInterests) {
            interests.add(i.getAsString());
        }

        //create a new attendee and added to the attendee database hashmap
        Attendee a = new Attendee(uid, name, interests);
        attendeedb.put(uid, a);

        return uid;
    }

    // validate a attendee by retrieving it again from the database and checking if the users account still exists or if their interest has changed
    public ResponseEntity<Object> validateAttendees(){

        RestTemplate restTemplate = new RestTemplate();

        // response array about each attendee's status after validation
        ArrayList<String> validatedUsers = new ArrayList();
        validatedUsers.add("Validating " +attendeedb.size() + "attendees...");
        Gson gson = new Gson();
        // list of which users to remove from database after validation if their account does not longer exist
        ArrayList<String> toRemove = new ArrayList();

        //for each attendee
        attendeedb.forEach((k,v) -> {
            String result = "";

            try{
                //retrieve user information, place it in a JSON object and compare against information stored within the database
                result = restTemplate.getForObject("https://pmaier.eu.pythonanywhere.com/user/" + k, String.class);
                String re = result.substring(91,result.length() -2);

                JsonElement element = gson.fromJson(re, JsonElement.class);
                JsonObject jsonObj = element.getAsJsonObject();

                JsonArray jsonInterests;
                ArrayList<String> interests = new ArrayList();
                jsonInterests = jsonObj.get("interests").getAsJsonArray();

                for (JsonElement i : jsonInterests) {
                    interests.add(i.getAsString());
                }
                // replace attendees interests with newly acquired interests
                v.setInterests(interests);
                String name = v.getName();
                validatedUsers.add("Validated: " + name);
            }
            // if the attempt to retrieve a given user fails due to not existing anymore, add according message to response message, and remove attendee from all events.
            catch(Exception e){

                String name = v.getName();

                validatedUsers.add("Cancelled: " + name);

                eventService.getEventdb().forEach((key,value) -> {
                    if(value.isAttending(k)){
                        value.removeFromEvent(k);
                    }
                });
                // add attendee to a array to remove it after all validation across the hashmap is complete to avoid errors
                toRemove.add(k);
            }
        });
        // remove every attendee that no longer exists from the attendee database hashmap
        for (String uid: toRemove) {
            attendeedb.remove(uid);
        }

        // create appropriate response message
        ResponseEntity<Object> response = createResponse.response(null,HttpStatus.OK , validatedUsers);
        return response;
    }
}
