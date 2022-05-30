package com.example.service.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {

    String eid;
    String description;
    String location;
    LocalDate date;
    LocalTime time;
    int duration;
    int capacity;
    int attending;

    ArrayList<String> attendees = new ArrayList<>();

    // Standard constructor required event ID, location, date, time, duration,max capacity
    public Event(String eventID, String de, String loc, LocalDate da, LocalTime ti, int dur, int maxCapacity) {

        eid= eventID;
        description= de;
        location= loc;
        date= da;
        time= ti;
        duration = dur;
        capacity = maxCapacity;
        attending = 0;
    }

   // event id getter
    public String geteid() {
        return eid;
    }
    // event description getter
    public String getDescription() {
        return description;
    }
    // event location getter
    public String getLocation() {
        return location;
    }
    // event date getter
    public LocalDate getDate() {
        return date;
    }
    // event time getter
    public LocalTime getTime() {
        return time;
    }
    // event duration getter
    public int getDuration() {
        return duration;
    }
    // event capacity getter
    public int getCapacity() {
        return capacity;
    }
    // event number attendees getter
    public int getAttending() {
        return attending;
    }
    // event ID setter
    public void setEventID(String e) {
        eid = e;
    }
    // event description setter
    public void setDescription(String de) {
         description = de;
    }
    // event location setter
    public void setLocation(String loc) {
         location = loc;
    }
    // event date setter
    public void setDate(LocalDate d) {
        date = d;
    }
    // event time setter
    public void setTime(LocalTime t) {
         time = t;
    }
    // event duration setter
    public void getDuration(int d) {
         duration = d;;
    }
    // event capacity setter
    public void setCapacity(int c) {
         capacity = c;
    }
    // event number of attendees setter
    public void setAttending(int at) {
         attending = at;
    }

    // add user to event and increase attending count by one
    public void attending(String userID) {
        attendees.add(userID);
        attending++;
    }

    // remove user from event and decrease attending count by one
    public void removeFromEvent(String userID) {
        attendees.remove(userID);
        attending--;
    }

    // check if a particular user is attending event
    public boolean isAttending(String userID) {
        System.out.println(attendees.size());
        for(int  i = 0;i < attendees.size(); i++){
            if(attendees.get(i).equals(userID)){
                return true;
            }
        }
        return false;
    }

    // get attendees attending the event
    public ArrayList<String> retrieveAttendees() {
        return attendees;
    }

    @Override
    public String toString() {
        return  eid + ", " +
                description + ", " +
                location + ", " +
                date + ", " +
                time + ", " +
                duration + ", " +
                capacity + ", " +
                attending;
    }
}
