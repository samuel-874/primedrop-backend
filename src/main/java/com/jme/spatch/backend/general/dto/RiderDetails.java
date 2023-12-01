package com.jme.spatch.backend.general.dto;


import com.jme.spatch.backend.model.order.entity.OrderDto;
import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import com.jme.spatch.backend.model.rider.entity.DeliveryLocation;
import com.jme.spatch.backend.model.rider.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderDetails {

    private String fullName;
    private String phoneNum;
    private long registrationDate;
    private String imagePath;
    private DeliveryLocation location;
    private int completedOrders;
    private int cancelledOrders;
    private int currentJobs;
    private VehicleType vehicle;
    private ActiveStatus statusActive;
    private List<OrderDto> orderDtoList;
    private int currentMonthRevenue;
    private int lastMonthRevenue;



}

