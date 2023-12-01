package com.jme.spatch.backend.model.user.service;

import com.jme.spatch.backend.model.admins.service.AdminRepository;
import com.jme.spatch.backend.model.user.entity.Role;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhoneNo) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailOrPhoneNo(usernameOrPhoneNo, usernameOrPhoneNo);
        String userRole = user.getRole().toString();
        if (user != null) {
            User authUser = null;
            if(user.getRole() == Role.ROLE_USER){
                authUser = new User(
                        user.getEmail(),
                        user.getPassword(),
                        Stream.of(user.getRole())
                                .map( role -> new SimpleGrantedAuthority(userRole))
                                .collect(Collectors.toList()));

            }else if(user.getRole() == Role.ROLE_RIDER) {
                authUser = new User(
                        user.getPhoneNo(),
                        user.getPassword(),
                        Stream.of(user.getRole())
                                .map(role -> new SimpleGrantedAuthority(userRole))
                                .collect(Collectors.toList()));

            } else if (user.getRole() == Role.ROLE_ADMIN || user.getRole() == Role.ROLE_SUPER_ADMIN) {
                authUser = new User(
                        user.getEmail(),
                        user.getPassword(),
                        Stream.of(user.getRole())
                                .map(role -> new SimpleGrantedAuthority(userRole))
                                .collect(Collectors.toList()));
            }
            return authUser;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
