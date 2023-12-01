package com.jme.spatch.backend.general.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileUpdateRequest {

    private String fullName;

    private String phoneNo;

    private String email;

}
