package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import com.jme.spatch.backend.model.rider.entity.DeliveryLocation;
import com.jme.spatch.backend.model.user.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderDto {

    private long   id;
    private String fullName;
    private String phoneNum;
    private BigDecimal balance;
    private String imagePath;
    private String plateNo;
    private DeliveryLocation location;
    private ActiveStatus activeStatus;
    private Status status;
    private int pendingJobs;
    private int pickedUpJobs;
    private int deliveredJobs;

}
