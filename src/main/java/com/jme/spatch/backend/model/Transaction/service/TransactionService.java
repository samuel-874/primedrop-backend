package com.jme.spatch.backend.model.Transaction.service;

import com.jme.spatch.backend.general.dto.TransactionRequest;
import com.jme.spatch.backend.model.Transaction.entity.Transaction;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    ResponseEntity createTransaction(HttpServletRequest servletRequest, TransactionRequest transactionRequest);

    ResponseEntity initializeTransaction(
            HttpServletRequest servletRequest,
            TransactionRequest transactionRequest
    );

    ResponseEntity completeTran(
            HttpServletRequest servletRequest, long transactionId, long fwTId);

    ResponseEntity viewTransactions(HttpServletRequest servletRequest);
    ResponseEntity getAllTransactions();

    List<Transaction> getTransactions(UserEntity user);


}
