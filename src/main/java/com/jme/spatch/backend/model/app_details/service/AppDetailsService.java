package com.jme.spatch.backend.model.app_details.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AppDetailsService {

    ResponseEntity getAppDetails();
}
