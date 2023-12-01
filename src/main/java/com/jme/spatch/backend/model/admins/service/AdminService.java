package com.jme.spatch.backend.model.admins.service;

import com.jme.spatch.backend.general.dto.AdminUpdateRequest;
import com.jme.spatch.backend.general.dto.AuthenticationRequest;
import com.jme.spatch.backend.model.admins.entity.AdminRegistration;
import com.jme.spatch.backend.model.user.service.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    ResponseEntity registerAdmin(AdminRegistration registration, HttpServletRequest servletRequest);
    ResponseEntity registerSuperAdmin(AdminRegistration registration);

    ResponseEntity logAdminIn(AuthenticationRequest authRequest);

    ResponseEntity getDetails(HttpServletRequest servletRequest);

    ResponseEntity sendResetPasswordOtp(String email);

    ResponseEntity resetPassword(PasswordResetRequest request);

    ResponseEntity getAppData(HttpServletRequest servletRequest);


    ResponseEntity getAllUsers();

    ResponseEntity getAllRiders();

    ResponseEntity getAllAdmins();

    ResponseEntity getUserDetails(long id);

    ResponseEntity updateAdmin(AdminUpdateRequest updateRequest);
}
