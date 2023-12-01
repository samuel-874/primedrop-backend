package com.jme.spatch.backend.model.token.service;


import com.jme.spatch.backend.model.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Token findByOrigin(String email);
    Token findByOtp(String otp);
}
