package com.jme.spatch.backend.model.app_details.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppDetails {
    private int numOfRider;
    private int numOfUser;
    private int numOfAdmin;
    private BigDecimal allTimeRevenue;
    private int allTimeJob;
    private String toDaysJobs;
}
