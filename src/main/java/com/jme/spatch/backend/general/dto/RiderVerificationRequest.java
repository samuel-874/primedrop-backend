package com.jme.spatch.backend.general.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiderVerificationRequest {

    @Length(min = 6, max = 6,message = "Invalid otp")
    private String otp;
    @NotBlank
    private String origin;
}
