package com.jme.spatch.backend.model.user.service;

import com.jme.spatch.backend.general.dto.*;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Service
public interface UserService  {

     ResponseEntity sendVerificationMail(String email) throws MessagingException, UnsupportedEncodingException;

    Boolean isEmailAvailable(String email);

    ResponseEntity reSendVerificationMail(
            String email
    ) throws
         MessagingException,
         UnsupportedEncodingException;

    ResponseEntity verifyEmail(String email, String otp);

     ResponseEntity sendMobileOtp(RegCompletionRequest completionRequest,String email);

    ResponseEntity reSendMobileOtp(
            MobileVerificationRequest vRequest);

    ResponseEntity verifyMobileNum(MobileVerificationRequest verificationRequest);

    ResponseEntity logUserIn(AuthenticationRequest request);

    ResponseEntity sendResetPasswordToken(String email) throws MessagingException, UnsupportedEncodingException;



    ResponseEntity resetPassword(PasswordResetRequest passwordRequest);

    ResponseEntity updatePassword(PasswordUpdateRequest updateRequest, HttpServletRequest servletRequest);

    ResponseEntity updateProfile(ProfileUpdateRequest updateRequest, HttpServletRequest servletRequest);

    ResponseEntity getDetails(
            HttpServletRequest servletRequest);

    List<UserEntity> getAllUsers();
}
