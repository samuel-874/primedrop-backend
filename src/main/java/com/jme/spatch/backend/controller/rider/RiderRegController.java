package com.jme.spatch.backend.controller.rider;

import com.jme.spatch.backend.general.dto.AuthenticationRequest;
import com.jme.spatch.backend.general.dto.ResetDto;
import com.jme.spatch.backend.general.dto.RiderVerificationRequest;
import com.jme.spatch.backend.model.rider.entity.RiderRegRequest;
import com.jme.spatch.backend.model.rider.service.RiderService;
import com.jme.spatch.backend.model.user.service.PasswordResetRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/rider/spatch/reg")
@AllArgsConstructor
public class RiderRegController {

    private final RiderService riderService;


    @PostMapping("save")
    public ResponseEntity registerRider(
          @Valid  @RequestBody RiderRegRequest regRequest){
        return riderService.saveRider(regRequest);
    }

    @PostMapping("verify")
    public ResponseEntity verifyRider(
            @RequestBody RiderVerificationRequest verificationRequest
            ){
        return riderService.verifyRider(verificationRequest);
    }

    @PostMapping("resend")
    public ResponseEntity resendVerificationOtp(
            @RequestBody ResetDto resetDto
            ){
        return riderService.reSendMobileOtp(resetDto);
    }

    @PostMapping("send-reset")
    public ResponseEntity sendPasswordResetOtp(
            @RequestParam("mobile") String phoneNum
        ) throws
            MessagingException, UnsupportedEncodingException {
        return riderService.sendResetPasswordOtp(phoneNum);
    }

    @PostMapping("password-reset")
    public ResponseEntity resetPassword(
           @Valid @RequestBody PasswordResetRequest resetRequest
            ){
        return riderService.resetPassword(resetRequest);
    }

    @PostMapping("signIn")
    public ResponseEntity logRiderIn(
           @Valid @RequestBody AuthenticationRequest authenticationRequest
            ){
        return riderService.logRiderIn(authenticationRequest);
    }



}
