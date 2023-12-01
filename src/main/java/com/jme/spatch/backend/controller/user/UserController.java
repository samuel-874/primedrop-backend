package com.jme.spatch.backend.controller.user;

import com.jme.spatch.backend.general.dto.PasswordUpdateRequest;
import com.jme.spatch.backend.general.dto.ProfileUpdateRequest;
import com.jme.spatch.backend.model.settings.service.SettingsService;
import com.jme.spatch.backend.model.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/auth/spatch/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final SettingsService settingsService;
    private final HttpServletRequest servletRequest;


    @PostMapping("/password_update")
    public ResponseEntity updatePassword(
            @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest){
       return userService.updatePassword(passwordUpdateRequest,servletRequest);
    }

    @GetMapping("tester")
    public ResponseEntity testMe(){
        return ResponseEntity.ok("Am tested");
    }

    @PostMapping("/profile_update")
    public ResponseEntity updateProfile(
            @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest){
       return userService.updateProfile(profileUpdateRequest,servletRequest);
    }


        @GetMapping
    public ResponseEntity viewProfile(){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            System.out.println("User roles: " + authorities);
            return userService.getDetails(servletRequest);
    }

    @GetMapping("/get-settings")
    public ResponseEntity getAll(){
        return settingsService.getAllSettings();
    }

        @GetMapping("/get-app-settings")
    public ResponseEntity getAppSettings(){
        return settingsService.getAppSettings();
    }






}
