package com.jme.spatch.backend.controller.admin;

import com.jme.spatch.backend.general.dto.AuthenticationRequest;
import com.jme.spatch.backend.model.admins.entity.AdminRegistration;
import com.jme.spatch.backend.model.admins.service.AdminService;
import com.jme.spatch.backend.model.user.service.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin/spatch/reg")
public class AdminRegController {
    private final AdminService adminService;
    private final HttpServletRequest servletRequest;


    @PostMapping("/admin/save")
    public ResponseEntity registerSuperAdmin(@Valid @RequestBody AdminRegistration adminRegistration){
        return adminService.registerSuperAdmin(adminRegistration);
    }

    @PostMapping("/save")
    public ResponseEntity registerAdmin(@Valid @RequestBody AdminRegistration adminRegistration){
        return adminService.registerAdmin(adminRegistration,servletRequest);
    }

    @PostMapping("/login")
    public ResponseEntity logIn(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        return adminService.logAdminIn(authenticationRequest);
    }

    @PostMapping("/send_otp")
    public ResponseEntity sendResetPasswordOtp(@RequestParam String email){
        return adminService.sendResetPasswordOtp(email);
    }

    @PostMapping("/reset_password")
    public ResponseEntity resetPassword(@RequestBody PasswordResetRequest request){
        return adminService.resetPassword(request);
    }






}
