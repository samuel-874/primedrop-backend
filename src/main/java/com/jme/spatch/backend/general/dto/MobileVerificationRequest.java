package com.jme.spatch.backend.general.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileVerificationRequest {
    @Size(min = 11, max = 14, message = "Invalid mobile no.")
    private String mobile;
    @Size(min = 6, max = 6, message = "Invalid otp")
    private String otp;
}
