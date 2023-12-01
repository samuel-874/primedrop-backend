package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.Transaction.entity.Transaction;
import com.jme.spatch.backend.model.order.entity.OrderDto;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private String fullName;
    private long registrationDate;
    private String email;
    private String phoneNum;
    private String imagePath;
    private BigDecimal balance;
    private int completedOrders;
    private int cancelledOrders;
    private List<OrderDto> orderDtoList = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<UserEntity> refferred = new ArrayList<>();
}
