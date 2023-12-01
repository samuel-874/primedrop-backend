package com.jme.spatch.backend.model.order.service;


import com.jme.spatch.backend.general.dto.OrderDetails;
import com.jme.spatch.backend.general.dto.OrderRequest;
import com.jme.spatch.backend.general.dto.OrderUpdateRequest;
import com.jme.spatch.backend.general.mapper.DtoMapper;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.Transaction.service.TransactionRepository;
import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.order.entity.OrderDto;
import com.jme.spatch.backend.model.order.entity.OrderStatus;
import com.jme.spatch.backend.model.order.entity.UserOrderDto;
import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.rider.service.RiderRepository;
import com.jme.spatch.backend.model.rider.service.RiderService;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.UserRepository;
import com.jme.spatch.backend.model.user.service.UserService;
import com.jme.spatch.backend.model.user.service.UserUtils;
import com.jme.spatch.backend.model.wallet.service.WalletRepository;
import com.jme.spatch.backend.model.wallet.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.jme.spatch.backend.general.mapper.DtoMapper.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final RiderRepository riderRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final RiderService riderService;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final UserUtils userUtils;

    @Override
    public ResponseEntity createOrder(
            OrderRequest orderRequest,
            HttpServletRequest servletRequest
    ) {

        Order order = DtoMapper.mapOrder(orderRequest);
        UserEntity user = userUtils.extractUser(servletRequest);
             order.setUser(user);
             int charge = orderRequest.getCharge();
             int balance = walletService.getBalance(user);

        if(charge > balance){
            return ResponseHandler.handle(400,
                    "Insufficient fund", null);
        }
            order.setCharge(charge);

            order.setOrderStatus(OrderStatus.PENDING);
            order.setOrderDate(System.currentTimeMillis());
            order.setAlphaNumericId(generateAlphanumeric());

            walletService.removeMoney(user,charge);
        RiderEntity rider = riderService.getRiderToDeliver();
        if(rider != null){
            order.setRider(rider);
        }

            orderRepository.save(order);
        return ResponseHandler.handle(201,"Order placed successfully", order);
    }

    @Override
    public ResponseEntity startOrder(String orderId){
        Optional <Order> orderOptional = orderRepository.findByAlphaNumericId(orderId);
            if(orderOptional.isEmpty()){
             return ResponseHandler.handle(400,"No order found for id "+ orderId,null);
            }


        Order order = orderOptional.get();
            if(order.getOrderStatus() != OrderStatus.PENDING){
                return ResponseHandler.handle(403,"Action not allowed",null);

            }
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setPickUpTime(LocalTime.now());
        orderRepository.save(order);

        return ResponseHandler.handle(200,"Order updated",order);
    }

    @Override
    public ResponseEntity completeOrder(String orderId){
        Optional <Order> orderOptional = orderRepository.findByAlphaNumericId(orderId);
        if(orderOptional.isEmpty()){
            return ResponseHandler.handle(400,"No order found for id "+ orderId,null);
        }
        Order order = orderOptional.get();
            if(order.getOrderStatus() != OrderStatus.PROCESSING){
                return ResponseHandler.handle(403,"Action not allowed",null);
            }
        order.setOrderStatus(OrderStatus.DONE);
        order.setDropOffTime(LocalTime.now());
          orderRepository.save(order);
        return ResponseHandler.handle(200,"Order completed ", order);
    }

    @Override
    public ResponseEntity cancelOrder(String orderId) {
        Optional <Order> orderOptional = orderRepository.findByAlphaNumericId(orderId);
        if(orderOptional.isEmpty()){
            return ResponseHandler.handle(400,"No order found for id "+ orderId,null);
        }

        Order order = orderOptional.get();
        if(order.getOrderStatus() == OrderStatus.DONE){
            return ResponseHandler.handle(403,"Action not allowed",null);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return ResponseHandler.handle(200,"Order canceled", null);
    }

    @Override
    public ResponseEntity getOrder(
            String orderId,
            HttpServletRequest servletRequest) {
        Optional <Order> orderOptional = orderRepository.findByAlphaNumericId(orderId);
        if(orderOptional.isEmpty()){
            return ResponseHandler.handle(400,"No order found for id "+ orderId,null);
        }

        Order order = orderOptional.get();
        UserEntity user = userUtils.extractUser(servletRequest);
        if(order.getUser() != user){
            return ResponseHandler.handle(403,"This order was not made by the user",null);
        }

        return ResponseHandler.handle(200,"Order retrieved successfully", order);
    }

    @Override
    public ResponseEntity getOrderAdmin(String orderId){
        Optional <Order> orderOptional = orderRepository.findByAlphaNumericId(orderId);
        if(orderOptional.isEmpty()){
            return ResponseHandler.handle(400,"No order found for id "+ orderId,null);
        }

        Order order = orderOptional.get();
        OrderDetails orderDetails  = mapOrderDetails(order);

        List<RiderEntity> activeRiders = riderService.getAllActiveRiders();
        List<UserOrderDto> activeRidersDto = activeRiders.stream().map(rider -> mapUserOrderDto(rider)).collect(Collectors.toList());

            orderDetails.setActiveRiders(activeRidersDto);
        return ResponseHandler.handle(200,"Order retrieved successfully", orderDetails);
    }



    @Override
    public ResponseEntity getOrderByIdForRider(
            String orderId,
            HttpServletRequest servletRequest) {
        RiderEntity rider = userUtils.extractRider(servletRequest);
            if(rider == null){
                return ResponseHandler.handle(404,"Endpoint is Only for Rider's", null);
            }

         Optional<Order> order = orderRepository.findByAlphaNumericId(orderId);
            if(order.isEmpty()){
                return ResponseHandler.handle(404,"Order not found", null);
            }
      return ResponseHandler.handle(200,"Rider's Order retrieved successfully",order.get());
    }

    @Override
    public ResponseEntity updateOrder(OrderUpdateRequest updateRequest) {
        Optional<Order> optionalOrder = orderRepository.findByAlphaNumericId(updateRequest.getAlphaNumericId());
        if(optionalOrder.isEmpty()){
            return ResponseHandler.handle(404,"Order not found",null);

        }

        Order order = optionalOrder.get();
        if(updateRequest.getOrderStatus() != null){
            order.setOrderStatus(updateRequest.getOrderStatus());
        }
        if(updateRequest.getRiderId() != 0){
            Optional<RiderEntity> rider = riderRepository.findById(updateRequest.getRiderId());
            if(rider.isEmpty()){
                return ResponseHandler.handle(404,"Rider not found",null);
            }
            order.setRider(rider.get());
        }
        orderRepository.save(order);
        return ResponseHandler.handle(200,"Update was Successful",order);
    }

    @Override
    public ResponseEntity getAllOrder(
            HttpServletRequest servletRequest) {
        UserEntity user = userUtils.extractUser(servletRequest);
        List<Order> order = orderRepository.findByUser(user);
        return ResponseHandler.handle(200,"Order retrieved successfully", order);
    }
        @Override
    public ResponseEntity getAllOrdersAdmin(
                HttpServletRequest servletRequest) {
        List<Order> order = orderRepository.findAll();
            List<OrderDto> orderDtos =  new ArrayList<>() ;
            for(Order eachOrder : order){
                OrderDto orderDto = mapOrderDto(eachOrder);
                orderDtos.add(orderDto);
            }

            return ResponseHandler.handle(200,"Order retrieved successfully", orderDtos);
    }

    @Override
    public List<Order> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }

    @Override
    public List<OrderDto> getAllOrder(UserEntity user) {
       List<Order> orderList = orderRepository.findByUser(user);
       List<OrderDto> orderDtoList = orderList.stream().map(order -> mapOrderDto(order)).collect(Collectors.toList());
        return orderDtoList;
    }

    @Override
    public ResponseEntity getAllRidersOrder(
            HttpServletRequest servletRequest
    ){
        RiderEntity rider = userUtils.extractRider(servletRequest);
            if(rider == null){
                return ResponseHandler.handle(403,"Endpoint is only for Rider",null);
            }
        List<Order> orders = orderRepository.findByRider(rider);
            if(orders.isEmpty()){
                return ResponseHandler.handle(200,"No Orders  yet",null);
            }
        return ResponseHandler.handle(200,"Orders retrieved successfully",orders);

    }

    public String generateAlphanumeric() {
    final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        Order order = null;

        do{
            for (int i = 0; i < 6; i++){
                int index = random.nextInt(CHARACTERS.length());
                builder.append(CHARACTERS.charAt(index));
            }
            try{
                 order = orderRepository.findByAlphaNumericId(builder.toString()).get();
            }catch (Exception e){

            }

        }while (order != null);


        return builder.toString();
    }

}
