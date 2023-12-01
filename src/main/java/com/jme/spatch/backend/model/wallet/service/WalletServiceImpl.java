package com.jme.spatch.backend.model.wallet.service;

import com.jme.spatch.backend.general.dto.FundRequest;
import com.jme.spatch.backend.model.Transaction.entity.Transaction;
import com.jme.spatch.backend.model.Transaction.entity.TransactionStatus;
import com.jme.spatch.backend.model.Transaction.entity.TransactionType;
import com.jme.spatch.backend.model.Transaction.service.TransactionRepository;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.UserUtils;
import com.jme.spatch.backend.model.wallet.entity.Wallet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final UserUtils userUtils;
    private final TransactionRepository transactionRepository;

    @Autowired
    public WalletServiceImpl(
            WalletRepository walletRepository,
            UserUtils userUtils,
            TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userUtils = userUtils;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public ResponseEntity addMoney(
            HttpServletRequest servletRequest,
            int amount,String source
    ){
        UserEntity user = userUtils.extractUser(servletRequest);
            if(user == null){
                return ResponseHandler.handle(403,"User not found or Token invalid",null);
            }

        Wallet wallet = walletRepository.findByUser(user);
            if(wallet == null){
                return ResponseHandler.handle(403,"Wallet has not been initialized",null);
            }

        BigDecimal balance = wallet.getBalance();
        Transaction transaction = new Transaction();
            transaction.setType(TransactionType.FUNDING);
            transaction.setAmount(BigDecimal.valueOf(amount));
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setSource(source);
            transaction.setDescription("Wallet funding");
            transaction.setDate(LocalDateTime.now());
            transaction.setUser(user);
            transactionRepository.save(transaction);

         BigDecimal newBalance =   balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newBalance);
       Wallet updatedWallet =  walletRepository.save(wallet);
        return ResponseHandler.handle(200,"Wallet incremented successfully",wallet.getBalance());
    }

    @Override
    public void addMoney(
            UserEntity user,
            int amount, String source,
            String description) throws InvalidCredentialsException {

            if(user == null){
                throw new NullPointerException("User not found");
            }

        Wallet wallet = walletRepository.findByUser(user);
            if(wallet == null){
                throw new InvalidCredentialsException("Wallet has\'s been init");
            }

        BigDecimal balance = wallet.getBalance();
        Transaction transaction = new Transaction();
            transaction.setType(TransactionType.FUNDING);
            transaction.setAmount(BigDecimal.valueOf(amount));
            transaction.setSource(source);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription(description);
            transaction.setDate(LocalDateTime.now());
            transaction.setUser(user);
            transactionRepository.save(transaction);

         BigDecimal newBalance =   balance.add(BigDecimal.valueOf(amount));
         wallet.setBalance(newBalance);
         walletRepository.save(wallet);
    }


    @Override
    public void removeMoney(
            UserEntity user,
            int amount, String source,
            String description) throws InvalidCredentialsException, IllegalAccessException {

            if(user == null){
                throw new NullPointerException("User not found");
            }

        Wallet wallet = walletRepository.findByUser(user);
            if(wallet == null){
                throw new InvalidCredentialsException("Wallet has\'s been init");
            }

        BigDecimal balance = wallet.getBalance();
        Transaction transaction = new Transaction();
            transaction.setType(TransactionType.DEDUCTING);
            transaction.setAmount(BigDecimal.valueOf(amount));
            transaction.setSource(source);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription(description);
            transaction.setDate(LocalDateTime.now());
            transaction.setUser(user);
            transactionRepository.save(transaction);

         BigDecimal newBalance =   balance.subtract(BigDecimal.valueOf(amount));
        if(newBalance.longValue() < 1){
            throw new IllegalAccessException("Insuffiect Fund");
        }
         wallet.setBalance(newBalance);
         walletRepository.save(wallet);
    }


        @Override
    public ResponseEntity addMoney(
            HttpServletRequest servletRequest,
            int amount
    ){
        UserEntity user = userUtils.extractUser(servletRequest);
            if(user == null){
                return ResponseHandler.handle(403,"User not found or Token invalid",null);
            }

        Wallet wallet = walletRepository.findByUser(user);
            if(wallet == null){
                return ResponseHandler.handle(403,"Wallet has not been initialized",null);
            }

        BigDecimal balance = wallet.getBalance();

         BigDecimal newBalance =   balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newBalance);
       Wallet updatedWallet =  walletRepository.save(wallet);
        return ResponseHandler.handle(200,"Wallet incremented successfully",wallet.getBalance());
    }




    @Override
    public ResponseEntity removeMoney(HttpServletRequest servletRequest, int amount) {
        UserEntity user = userUtils.extractUser(servletRequest);
        if(user == null){
            return ResponseHandler.handle(403,"User not found or Token invalid",null);
        }

        Wallet wallet = walletRepository.findByUser(user);
        if(wallet == null){
            return ResponseHandler.handle(403,"Wallet has not been initialized",null);
        }

        BigDecimal balance = wallet.getBalance();

        BigDecimal updatedBalance = balance.subtract(BigDecimal.valueOf(amount)) ;
        if(updatedBalance.longValue() < 1){
            return ResponseHandler.handle(403,"Insufficient Fund",wallet.getBalance());
        }
        wallet.setBalance(updatedBalance);

        Wallet updatedWallet =  walletRepository.save(wallet);


        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEDUCTING);
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setSource("order");
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);

        return ResponseHandler.handle(200,"Fund deducted successfully",wallet.getBalance());
    }

       @Override
    public ResponseEntity removeMoney(UserEntity user, int amount) {

        Wallet wallet = walletRepository.findByUser(user);
        if(wallet == null){
            return ResponseHandler.handle(403,"Wallet has not been initialized",null);
        }

        BigDecimal balance = wallet.getBalance();
        BigDecimal updatedBalance = balance.subtract(BigDecimal.valueOf(amount)) ;
        wallet.setBalance(updatedBalance);
        Wallet updatedWallet =  walletRepository.save(wallet);


        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEDUCTING);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setSource("order");
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transactionRepository.save(transaction);

        return ResponseHandler.handle(200,"Fund deducted successfully",wallet.getBalance());
    }



    @Override
    public ResponseEntity getWallet(HttpServletRequest servletRequest) {
        UserEntity user = userUtils.extractUser(servletRequest);
        if(user == null){
            return ResponseHandler.handle(403,"User not found or Token invalid",null);
        }

        Wallet wallet = walletRepository.findByUser(user);
        if(wallet == null){
            return ResponseHandler.handle(403,"Wallet has not been initialized",null);
        }

        return ResponseHandler.handle(200,"Balance retrieved successfully",wallet);
    }


    @Override
    public int getBalance(UserEntity user){
        Wallet wallet = walletRepository.findByUser(user);
        BigDecimal bigDecimalBalance = wallet.getBalance();
        int balance = bigDecimalBalance.intValue();
        return balance;
    }

    @Override
    public void init(UserEntity user) {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(200000));
        wallet.setUser(user);
        walletRepository.save(wallet);
    }

    @Override
    public ResponseEntity fundWallet(FundRequest fundRequest) {
        UserEntity user = userUtils.getUser(fundRequest.getId());
        try{
            addMoney(user, fundRequest.getAmount(), fundRequest.getSource(),fundRequest.getDescription());
        }catch (Exception e){
            System.out.println(e);
            return ResponseHandler.handle(403,"An error occurred",null);
        }

        return ResponseHandler.handle(200,"Wallet Funded successfully",null);
    }



    @Override
    public ResponseEntity deductFund(FundRequest fundRequest) {
        UserEntity user = userUtils.getUser(fundRequest.getId());
        try{
            removeMoney(user, fundRequest.getAmount(), fundRequest.getSource(), fundRequest.getDescription() );
        }catch (IllegalAccessException e) {
                return ResponseHandler.handle(403,"Insufficient Fund",null);

        }
        catch(Exception e){
            System.out.println(e);
            return ResponseHandler.handle(403,"An error occurred",null);
        }

        return ResponseHandler.handle(200,"Fund Deducted successfully",null);
    }





}
