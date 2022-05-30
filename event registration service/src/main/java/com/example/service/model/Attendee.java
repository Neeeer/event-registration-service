// A resource, living in the model sub-package.

package com.example.service.model;

import java.util.ArrayList;

// Attendee object class
public class Attendee
{
    String uid;
    String name;
    ArrayList<String> interests;
    ArrayList<String> attendingEvents = new ArrayList<>();


    // Standard constructor required user ID, name and interests
    public Attendee(String ID,String n,ArrayList<String> inte) {
        uid =ID;
        name = n;
        interests = inte;
    }

    // user ID getter
    public String getUserID() {
        return uid;
    }
    // name getter
    public String getName() {
        return name;
    }
    // interests getter
    public ArrayList<String> getInterests() {
        return interests;
    }
    // user id setter
    public void setUserID(String u) {
        uid = u;
    }
    // name setter
    public void setName(String n) {
        name = n;
    }
    // interests setter
    public void setInterests(ArrayList<String> inte) {
        interests = inte;
    }

    // add event to users attending events list
    public void attendEvent(String event) {
        attendingEvents.add(event);
    }

    // remove event from users attending events list
    public void removeEvent(String event) {
        attendingEvents.remove(event);
    }

    // check if user is attending given event
    public boolean containsEvent(String eventID) {
        for(int  i = 0;i < attendingEvents.size(); i++){
            if(attendingEvents.get(i).equals(eventID)){
                return true;
            }
        }
        return false;
    }

    // getter for retrieving users attending events
    public ArrayList<String> retrieveAttendingEvents() {
        return attendingEvents;
    }

    @Override
    public String toString() {
        return  uid + ", " +
        name + ", " +
        interests;
    }
}
