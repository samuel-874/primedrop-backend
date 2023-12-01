package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.order.entity.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails {
    @Enumerated(EnumType.STRING)
    private PackageType requestType;
    @Enumerated(EnumType.STRING)
    private Vehicle vehicle;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    private String pickOffLocation;
    private String alphaNumericId;
    private String dropOffLocation;
    private int distance;
    private String averageDuration;
    private UserOrderDto user;
    private UserOrderDto receiver;
    private UserOrderDto sender;
    private UserOrderDto rider;
    private String itemToDeliver;
    private int itemQuantity;
    private String note;
    private LocalTime pickUpTime ;
    private LocalTime dropOffTime;
    private long orderDate;
    private int cashToReceive;
    private int charge;
    private List<UserOrderDto> activeRiders;
}
