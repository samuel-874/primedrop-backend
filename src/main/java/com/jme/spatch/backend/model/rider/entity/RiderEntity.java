package com.jme.spatch.backend.model.rider.entity;

import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RiderEntity extends UserEntity {

    @Enumerated(EnumType.STRING)
    private DeliveryLocation Location;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicle;
    @Enumerated(EnumType.STRING)
    private ActiveStatus statusActive;

    @ToString.Exclude
    @OneToMany(mappedBy = "rider", cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();
}
