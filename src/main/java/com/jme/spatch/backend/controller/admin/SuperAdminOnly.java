package com.jme.spatch.backend.controller.admin;

import com.jme.spatch.backend.general.dto.AdminUpdateRequest;
import com.jme.spatch.backend.general.dto.FundRequest;
import com.jme.spatch.backend.model.admins.service.AdminService;
import com.jme.spatch.backend.model.wallet.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin/spatch/sao")
public class SuperAdminOnly {

    private final AdminService adminService;
    private final WalletService walletService;


    @PutMapping("/update")
    public ResponseEntity updateAdmin(@RequestBody AdminUpdateRequest updateRequest){
        return adminService.updateAdmin(updateRequest);
    }
    @PutMapping("/fund")
    public ResponseEntity fundWallet(@RequestBody FundRequest fundRequest){
        return walletService.fundWallet(fundRequest);
    }

    @PutMapping("/deduct-fund")
    public ResponseEntity deductFund(@RequestBody FundRequest fundRequest){
        return walletService.deductFund(fundRequest);
    }




}
