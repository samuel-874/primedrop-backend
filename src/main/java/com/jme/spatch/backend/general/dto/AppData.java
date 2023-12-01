package com.jme.spatch.backend.general.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppData {
    private int todayRevenue;
    private int totalJobs;
    private int allTimeRevenue;
    private int allTimeJobs;
    private int pendingJobs;
    private int allUser;
    private int availableRiders;
}
