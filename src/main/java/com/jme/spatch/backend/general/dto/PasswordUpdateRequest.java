package com.jme.spatch.backend.general.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordUpdateRequest {
    @NotBlank(message = "Current password required")
    private String currentPassword;
    @NotBlank(message = "New password required ")
    private String newPassword;
}
