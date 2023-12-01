package com.jme.spatch.backend.controller.user;

import com.jme.spatch.backend.model.Transaction.service.TransactionService;
import com.jme.spatch.backend.general.dto.TransactionRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/spatch/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final HttpServletRequest servletRequest;


    @PostMapping("/create")
    public ResponseEntity createTransaction(
            @Valid  @RequestBody TransactionRequest transactionRequest){
        return transactionService.createTransaction(servletRequest,transactionRequest);
    }

      @PostMapping("/init")
    public ResponseEntity initializeTransaction(
            @Valid  @RequestBody TransactionRequest transactionRequest){
        return transactionService.initializeTransaction(servletRequest,transactionRequest);
    }

      @PostMapping("/complete-transaction")
    public ResponseEntity completeTransaction(
            @RequestParam long id,@RequestParam long fwId
      ){
        return transactionService.completeTran(servletRequest, id, fwId );
    }

    @GetMapping("")
    public ResponseEntity viewTransaction(){
        return transactionService.viewTransactions(servletRequest);
    }
}
