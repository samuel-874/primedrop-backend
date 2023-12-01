package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetails {
    private String fullName;
    private String email;
    private Role role;

}
