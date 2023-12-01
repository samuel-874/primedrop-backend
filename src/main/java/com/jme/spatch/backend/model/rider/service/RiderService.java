package com.jme.spatch.backend.model.rider.service;


import com.jme.spatch.backend.general.dto.*;
import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.rider.entity.RiderRegRequest;
import com.jme.spatch.backend.model.user.service.PasswordResetRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public interface RiderService {

     ResponseEntity saveRider(RiderRegRequest regRequest);
     ResponseEntity addRider(RiderRegRequest regRequest);
     ResponseEntity verifyRider(RiderVerificationRequest vRequest);
     ResponseEntity logRiderIn(AuthenticationRequest request);
     ResponseEntity reSendMobileOtp(ResetDto resetDto);
     ResponseEntity sendResetPasswordOtp(String phoneNo) throws MessagingException, UnsupportedEncodingException;
     ResponseEntity resetPassword(PasswordResetRequest passwordRequest);
     ResponseEntity updatePassword(PasswordUpdateRequest updateRequest, HttpServletRequest servletRequest);
     ResponseEntity updateProfile(ProfileUpdateRequest updateRequest, HttpServletRequest servletRequest);
     ResponseEntity getDetails( HttpServletRequest servletRequest);
     RiderEntity getRiderToDeliver();
     ResponseEntity getAllRiders();
     ResponseEntity getRiderDetails(long userId);
     List<RiderEntity> getAllActiveRiders();
}
