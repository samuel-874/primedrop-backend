package com.jme.spatch.backend.general.responsehandler;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity handle(int statusCode, String message,Object responseBody){
        Map<String,Object> response = new LinkedHashMap<>();

            response.put("error", statusCode >= 400);
            response.put("status_code",statusCode);
            response.put("message", message);
            response.put("data",responseBody);

    return ResponseEntity.status(HttpStatusCode.valueOf(statusCode)).body(response);
    }

}
