package com.jme.spatch.backend.model.app_details.service;

import com.jme.spatch.backend.model.user.entity.Role;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AppDetails implements AppDetailsService{
    private final UserRepository userRepository;


    @Override
    public ResponseEntity getAppDetails() {
        List<UserEntity> allUsers = userRepository.findAll();
        int numOfRiders = getRidersNum(allUsers);

        return null;
    }


    int getRidersNum(List<UserEntity> users){
        List<UserEntity> riders = new ArrayList<>();

        for(UserEntity user : users){
            if(user.getRole() == Role.ROLE_RIDER){
                riders.add(user);
            }
        }

        return riders.size();
    }
}
