package com.jme.spatch.backend.model.wallet.service;


import com.jme.spatch.backend.general.dto.FundRequest;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {

    ResponseEntity addMoney(HttpServletRequest servletRequest, int price,String source);

    void addMoney(
            UserEntity user,
            int amount, String source,
            String description) throws InvalidCredentialsException;

    void removeMoney(
            UserEntity user,
            int amount, String source,
            String description) throws InvalidCredentialsException, IllegalAccessException;

    ResponseEntity addMoney(
            HttpServletRequest servletRequest,
            int amount
    );

    ResponseEntity removeMoney(HttpServletRequest servletRequest, int price);

    ResponseEntity removeMoney(UserEntity user, int amount);

    ResponseEntity getWallet(HttpServletRequest servletRequest);

    int getBalance(UserEntity user);
    void init(UserEntity user);
    ResponseEntity fundWallet(FundRequest fundRequest);

    ResponseEntity deductFund(FundRequest fundRequest);
}
