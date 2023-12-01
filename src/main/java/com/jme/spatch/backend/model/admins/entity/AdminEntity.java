package com.jme.spatch.backend.model.admins.entity;

import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity extends UserEntity {
    private ActiveStatus activeStatus;
}