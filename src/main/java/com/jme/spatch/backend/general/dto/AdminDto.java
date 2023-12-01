package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import com.jme.spatch.backend.model.user.entity.Role;
import com.jme.spatch.backend.model.user.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private long id;
    private String fullName;
    private String email;
    private ActiveStatus activeStatus;
    private Status status;
    private Role role;
    private long registrationDate;

}
