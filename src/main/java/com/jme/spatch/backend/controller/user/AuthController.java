package com.jme.spatch.backend.controller.user;


import com.jme.spatch.backend.general.dto.AuthenticationRequest;
import com.jme.spatch.backend.general.dto.MobileVerificationRequest;
import com.jme.spatch.backend.general.dto.RegCompletionRequest;
import com.jme.spatch.backend.model.token.service.TokenService;
import com.jme.spatch.backend.model.user.service.PasswordResetRequest;
import com.jme.spatch.backend.model.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/spatch")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;


    @PostMapping("/verification/send")
    public ResponseEntity reg1(
            @RequestParam String email)
            throws
            MessagingException,
            UnsupportedEncodingException {
        return userService.sendVerificationMail(email);
    }

    @PostMapping("/verification/verify")
    public ResponseEntity reg2(
            @RequestParam("email") String email,
              @RequestParam("otp") String otp){
        return userService.verifyEmail(email,otp);
     }


    @PostMapping("/verification/verification_mobile")
    public ResponseEntity reg3(
               @Valid @RequestBody
               RegCompletionRequest completionRequest,
               @RequestParam("email") String email){
        return userService.sendMobileOtp(completionRequest, email);
    }

    @PostMapping("/verification/verify_mobile")
    public ResponseEntity reg4(
           @Valid @RequestBody
           MobileVerificationRequest verificationRequest){
        return userService.verifyMobileNum(verificationRequest);
    }

    @PostMapping("/verification/resend")
    public ResponseEntity resendEmailVerification(
            @RequestParam("email") String email
    ) throws MessagingException,
            UnsupportedEncodingException {
        return userService.reSendVerificationMail(email);
    }

    @PostMapping("/verification/resend_mobile")
    public ResponseEntity resendOtp(
            @RequestBody MobileVerificationRequest verificationRequest){
        return userService.reSendMobileOtp(verificationRequest);
    }


    @PostMapping("/authentication/authenticate_user")
    public ResponseEntity logIn(
            @Valid @RequestBody
            AuthenticationRequest authenticationRequest){
        return userService.logUserIn(authenticationRequest);
    }

    @RequestMapping("/verification/is_available")
    public Boolean isAvailable(
            @RequestParam("email") String email){
        return userService.isEmailAvailable(email);
    }


    @PostMapping("/user/reset_password/send")
    public ResponseEntity sendForgottenPasswordVerificationMail(
            String email
    ) throws
            MessagingException,
            UnsupportedEncodingException{
        return userService.sendResetPasswordToken(email);
    }


    @PostMapping("/user/reset_password")
    public ResponseEntity resetPassword(
          @Valid @RequestBody
          PasswordResetRequest passwordResetRequest){
        return userService.resetPassword(passwordResetRequest);
    }


}