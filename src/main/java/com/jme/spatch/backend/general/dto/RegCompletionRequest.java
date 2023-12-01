package com.jme.spatch.backend.general.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegCompletionRequest {
    @NotBlank(message = "full name is required")
    private String fullName;
    @Size(min = 6,message = "Invalid password")
    private String password;
    @Size(min = 11,message = "Mobile no. is required")
    private String phoneNo;
}
