package com.jme.spatch.backend.model.order.service;

import com.jme.spatch.backend.general.dto.OrderRequest;
import com.jme.spatch.backend.general.dto.OrderUpdateRequest;
import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.order.entity.OrderDto;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    ResponseEntity createOrder(OrderRequest orderRequest, HttpServletRequest servletRequest);

    ResponseEntity startOrder(String orderId);

    ResponseEntity completeOrder(String orderId);
    ResponseEntity cancelOrder(String orderId);

    ResponseEntity getOrder(String orderId, HttpServletRequest servletRequest);
    ResponseEntity getOrderAdmin(String orderId);

    ResponseEntity getOrderByIdForRider(String orderId, HttpServletRequest servletRequest);
    ResponseEntity updateOrder(OrderUpdateRequest updateRequest);
    ResponseEntity getAllOrder(HttpServletRequest servletRequest);

    ResponseEntity getAllOrdersAdmin(
            HttpServletRequest servletRequest);

    List<Order> getAllOrder();
    List<OrderDto> getAllOrder(UserEntity user);

    ResponseEntity getAllRidersOrder(
            HttpServletRequest servletRequest
    );


}
