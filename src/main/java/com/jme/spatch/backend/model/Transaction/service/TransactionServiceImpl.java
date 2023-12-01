package com.jme.spatch.backend.model.Transaction.service;

import com.jme.spatch.backend.general.dto.TransactionDto;
import com.jme.spatch.backend.model.Transaction.entity.Transaction;
import com.jme.spatch.backend.model.Transaction.entity.TransactionStatus;
import com.jme.spatch.backend.general.dto.TransactionRequest;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.UserUtils;
import com.jme.spatch.backend.model.wallet.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.jme.spatch.backend.general.mapper.DtoMapper.mapTransactionDto;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserUtils userUtils;
    private final WalletService walletService;


    @Override
    public ResponseEntity createTransaction(
            HttpServletRequest servletRequest,
            TransactionRequest transactionRequest
    ){
        Transaction transaction = new Transaction();
        UserEntity user = userUtils.extractUser(servletRequest);
            transaction.setSource(transactionRequest.getSource());
            transaction.setDescription(transactionRequest.getDescription());
            transaction.setAmount(BigDecimal.valueOf(transactionRequest.getAmount()));
            transaction.setDate(LocalDateTime.now());
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setUser(user);
            transaction.setType(transactionRequest.getType());
          transactionRepository.save(transaction);
        return ResponseHandler.handle(201,"Transaction successful", transaction);
     }
    @Override
    public ResponseEntity initializeTransaction(
            HttpServletRequest servletRequest,
            TransactionRequest transactionRequest
    ){
        Transaction transaction = new Transaction();
        UserEntity user = userUtils.extractUser(servletRequest);
            transaction.setSource(transactionRequest.getSource());
            transaction.setDescription(transactionRequest.getDescription());
            transaction.setAmount(BigDecimal.valueOf(transactionRequest.getAmount()));
            transaction.setDate(LocalDateTime.now());
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setUser(user);
            transaction.setType(transactionRequest.getType());
          transactionRepository.save(transaction);

        return ResponseHandler.handle(201,"Transaction successful", transaction);
     }


     @Override
     public ResponseEntity completeTran(
             HttpServletRequest servletRequest,
             long transactionId, long fwTId){
        Transaction transaction = transactionRepository.findById(transactionId).get();
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setFlutterWaveId(fwTId);

         transactionRepository.save(transaction);



         try{
             walletService.addMoney(servletRequest, transaction.getAmount().intValue());
         }catch (Exception e){
             return ResponseHandler.handle(500,"Transaction was not successful", transaction);
         }

         return ResponseHandler.handle(200,"Transaction successful", transaction);
     }

    @Override
    public ResponseEntity viewTransactions(
            HttpServletRequest servletRequest
    ){
        UserEntity user = userUtils.extractUser(servletRequest);
        List<Transaction> transactionList = transactionRepository.findByUser(user);
        return ResponseHandler.handle(200,"Transaction retrieved successfully", transactionList);
    }

    @Override
    public ResponseEntity getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDto> transactionDtoList = transactions.stream().map( transaction -> mapTransactionDto(transaction)).collect(Collectors.toList());
        return ResponseHandler.handle(200,"Transaction retrieved successfully", transactionDtoList );
    }

    @Override
    public List<Transaction> getTransactions(UserEntity user) {
        List<Transaction> transactionList = transactionRepository.findByUser(user);
        return transactionList;
    }
}
