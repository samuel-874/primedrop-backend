package com.jme.spatch.backend.general.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthenticationRequest {
    private String username;

    private String password;
}
