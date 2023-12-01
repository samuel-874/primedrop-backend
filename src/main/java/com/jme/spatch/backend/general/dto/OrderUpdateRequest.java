package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderUpdateRequest {
    private OrderStatus orderStatus;
    private String alphaNumericId;
    private long riderId;
}
