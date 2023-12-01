package com.jme.spatch.backend.model.user.service;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordResetRequest{
    @NotNull
    @Length(min = 6)
    private String password;
    private String emailOrPhoneNum;
    @NotNull
    private String otp;
}
