package com.example.service.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Response {

    // response method that creates a hashmap storing a message to be displayed in the browser, the http status from the request and a response object holding any information requested by the user.
    public ResponseEntity<Object> response(String msg, HttpStatus status, Object response) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", msg);
        map.put("status", status.value());
        map.put("data", response);
        return new ResponseEntity<>(map,status);
    }
}
