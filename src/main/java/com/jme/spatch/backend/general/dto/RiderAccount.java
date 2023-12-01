package com.jme.spatch.backend.general.dto;


import com.jme.spatch.backend.model.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderAccount {

    private String fullName;
    private String phoneNo;
    private BigDecimal balance;
    private List<Order> orderList;
    private String imagePath;

}
