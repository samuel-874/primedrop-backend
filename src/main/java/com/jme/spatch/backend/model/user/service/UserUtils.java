package com.jme.spatch.backend.model.user.service;

import com.jme.spatch.backend.config.jwt.JwtService;
import com.jme.spatch.backend.model.admins.entity.AdminEntity;
import com.jme.spatch.backend.model.admins.service.AdminRepository;
import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.rider.service.RiderRepository;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserUtils {
    private final    UserRepository userRepository;
    private final RiderRepository riderRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    public  UserEntity extractUser(
            HttpServletRequest servletRequest
    ){
        String authHeader = servletRequest.getHeader("Authorization");
        String jwt  =  authHeader.substring(7);
        String emailOrPhoneNum = jwtService.extractUsername(jwt);
        UserEntity user = userRepository.findByEmailOrPhoneNo(emailOrPhoneNum,emailOrPhoneNum);
        return  user;
    }

        public RiderEntity extractRider(
            HttpServletRequest servletRequest
      ){
        String authHeader = servletRequest.getHeader("Authorization");
        String jwt  =  authHeader.substring(7);
        String phoneNo = jwtService.extractUsername(jwt);
        RiderEntity rider = riderRepository.findByEmailOrPhoneNo(phoneNo,phoneNo);
        return  rider;
    }

        public AdminEntity extractAdmin(
           HttpServletRequest servletRequest
           ){
        String authHeader = servletRequest.getHeader("Authorization");
        String jwt  =  authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        AdminEntity admin = adminRepository.findByEmail(email);
        return  admin;
    }
         public UserEntity getUser(
              long id
           ){
        UserEntity user = userRepository.findById(id).get();
        return  user;
    }








}
