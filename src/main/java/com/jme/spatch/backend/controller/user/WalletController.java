package com.jme.spatch.backend.controller.user;

import com.jme.spatch.backend.model.wallet.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/spatch/wallet")
public class WalletController {

    private final WalletService walletService;
    private final HttpServletRequest servletRequest;

    @PostMapping("/add")
    public ResponseEntity addFund(
            @RequestParam int amount,String source){
        return walletService.addMoney(servletRequest,amount,source);
    }

    @PostMapping("/deduct")
    public ResponseEntity deductFund(
            @RequestParam int amount){
        return walletService.removeMoney(servletRequest,amount);
    }

    @GetMapping
    public ResponseEntity getWallet(){
        return walletService.getWallet(servletRequest);
    }


}
