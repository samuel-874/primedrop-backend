package com.jme.spatch.backend.model.route_cost.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteCost {

    @SequenceGenerator(
            name = "cost_seq",
            sequenceName = "cost_seq",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cost_seq")
    private long id;
    private double distanceFrom;
    private double distanceTo;
    private long amount;
    private int percentageIncrease;
}
