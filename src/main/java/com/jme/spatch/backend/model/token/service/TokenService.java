package com.jme.spatch.backend.model.token.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface TokenService {
    String generateToken();
    void sendOtpToMobileNum(String number);
    void sendOtpToEmail(String email) throws MessagingException, UnsupportedEncodingException;
    Boolean validateToken(String origin, String token);
    void deleteToken(String token);
}
