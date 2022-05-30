// Main application class, generated by Spring Initializr.
// Must live in the top package so that @SpringBootApplication annotation
// can find all the components.

package com.example.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.service.service.*;
import java.time.LocalDateTime;

@SpringBootApplication
public class ServiceApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    // The initDB() @Bean is run automatically on startup, before the
    // @RestController is started.
    // The body could access command line argument args (but doesn't).
    // The fact that initDB() requires attendee and event Services argument tells Spring
    @Bean
    public CommandLineRunner initDB(AttendeeService as, EventService es) {
        return (args) -> {
            //crete 3 events
            String E1 = "E1";
            String E2 = "E2";
            String E3 = "E3";
            es.createEvent(E1, "event 1", "hall 1", LocalDateTime.now(), 45, 1);
            es.createEvent(E2, "event 2", "hall 2", LocalDateTime.now(), 60, 5);
            es.createEvent(E3, "event 3", "hall 3", LocalDateTime.now(), 60, 10);

            // set references in event and attendee services to be able to reference each other
            as.setEventService(es.getEventService());
            es.setAttendeeService(as.getAttendeeService());

            // create 10 random attendees
            for (int i=0;i <10;i++){
                String a = as.createAttendee();
                double r1 = Math.floor(Math.random()*(3-1+1)+1);

                // add attendees from 1 to 3 random events
                for (int j=0;j <r1;j++){

                    double r2 = Math.floor(Math.random()*(3-1+1)+1);
                    if (r2 == 1) {
                        es.addUserToEvent(a,E1);
                    }
                    else if (r2 == 2) {
                        es.addUserToEvent(a,E2);
                    }
                    else {
                        es.addUserToEvent(a,E3);
                    }
                }
            }
        };
    }
}