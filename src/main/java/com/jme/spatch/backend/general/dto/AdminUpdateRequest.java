package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import com.jme.spatch.backend.model.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUpdateRequest {
    private long id;
    private Role role;
    private ActiveStatus activeStatus;
}
