package com.jme.spatch.backend.model.complain.service;

import com.jme.spatch.backend.general.dto.ComplainRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ComplainService {

    ResponseEntity contactSupport( HttpServletRequest servletRequest, ComplainRequest complainRequest);
    ResponseEntity viewReport(long id);
    ResponseEntity viewUserReports(HttpServletRequest servletRequest);

}
