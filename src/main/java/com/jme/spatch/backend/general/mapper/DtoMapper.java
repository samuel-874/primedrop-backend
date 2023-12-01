package com.jme.spatch.backend.general.mapper;

import com.jme.spatch.backend.general.dto.*;
import com.jme.spatch.backend.model.Transaction.entity.Transaction;
import com.jme.spatch.backend.model.admins.entity.AdminEntity;
import com.jme.spatch.backend.model.admins.entity.AdminRegistration;
import com.jme.spatch.backend.model.credit_card.entity.CreditCard;
import com.jme.spatch.backend.model.credit_card.entity.SaveCardRequest;
import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.order.entity.OrderDto;
import com.jme.spatch.backend.model.order.entity.OrderStatus;
import com.jme.spatch.backend.model.order.entity.UserOrderDto;
import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.rider.entity.RiderRegRequest;
import com.jme.spatch.backend.model.user.entity.Role;
import com.jme.spatch.backend.model.user.entity.Status;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.wallet.entity.Wallet;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class DtoMapper {


    public static Order mapOrder(OrderRequest orderRequest){
        Order order = Order.builder()
                .requestType(orderRequest.getRequestType())
                .pickOffLocation(orderRequest.getPickOffLocation())
                .dropOffLocation(orderRequest.getDropOffLocation())
                .distance(orderRequest.getDistance())
                .sendersName(orderRequest.getSendersName())
                .receiversName(orderRequest.getReceiversName())
                .senderPhoneNum(orderRequest.getSenderPhoneNum())
                .receiverPhoneNum(orderRequest.getReceiverPhoneNum())
                .vehicle(orderRequest.getVehicle())
                .averageDuration(orderRequest.getAverageDuration())
                .receiversName(orderRequest.getReceiversName())
                .sendersName(orderRequest.getSendersName())
                .receiverPhoneNum(orderRequest.getReceiverPhoneNum())
                .senderPhoneNum(orderRequest.getSenderPhoneNum())
                .itemToDeliver(orderRequest.getItemToDeliver())
                .itemQuantity(orderRequest.getItemQuantity())
                .note(orderRequest.getNote())
                .cashToReceive(orderRequest.getCashToReceive())
                .build();

        return order;
    }

    public static OrderDto mapOrderDto(Order order){

        OrderDto  orderDto = OrderDto.builder()
                .alphaNumericId(order.getAlphaNumericId())
                .requestType(order.getRequestType())
                .orderStatus(order.getOrderStatus())
                .charge(order.getCharge())
                .distance(order.getDistance())
                .pickUpTime(order.getPickUpTime())
                .dropOffTime(order.getDropOffTime())
                .pickOffLocation(order.getPickOffLocation())
                .dropOffLocation(order.getDropOffLocation())
                .distance(order.getDistance())
                .sender(new UserOrderDto(order.getSendersName(),order.getSenderPhoneNum(),null))
                .receiver(new UserOrderDto(order.getReceiversName(),order.getReceiverPhoneNum(),null))
                .user(mapUserOrderDto(order.getUser()))
                .rider(mapUserOrderDto(order.getRider()))
                .vehicle(order.getVehicle())
                .averageDuration(order.getAverageDuration())
                .itemToDeliver(order.getItemToDeliver())
                .itemQuantity(order.getItemQuantity())
                .orderDate(order.getOrderDate())
                .note(order.getNote())
                .cashToReceive(order.getCashToReceive())
                .build();


        return orderDto;

    }

      public static OrderDetails mapOrderDetails(Order order){

        OrderDetails  orderDetails = OrderDetails.builder()
                .alphaNumericId(order.getAlphaNumericId())
                .requestType(order.getRequestType())
                .orderStatus(order.getOrderStatus())
                .charge(order.getCharge())
                .distance(order.getDistance())
                .pickUpTime(order.getPickUpTime())
                .dropOffTime(order.getDropOffTime())
                .pickOffLocation(order.getPickOffLocation())
                .dropOffLocation(order.getDropOffLocation())
                .distance(order.getDistance())
                .sender(new UserOrderDto(order.getSendersName(),order.getSenderPhoneNum(),null))
                .receiver(new UserOrderDto(order.getReceiversName(),order.getReceiverPhoneNum(),null))
                .user(mapUserOrderDto(order.getUser()))
                .rider(mapUserOrderDto(order.getRider()))
                .vehicle(order.getVehicle())
                .averageDuration(order.getAverageDuration())
                .itemToDeliver(order.getItemToDeliver())
                .itemQuantity(order.getItemQuantity())
                .orderDate(order.getOrderDate())
                .note(order.getNote())
                .cashToReceive(order.getCashToReceive())
                .build();


        return orderDetails;

    }



    public static UserOrderDto mapUserOrderDto(UserEntity user){
        UserOrderDto userOrderDto = new UserOrderDto(user.getId(),user.getFullName(), user.getPhoneNo(), user.getEmail());
        return userOrderDto;
    }
        public static UserTransactionDto mapUserTransactionDto(UserEntity user){
        UserTransactionDto userOrderDto = new UserTransactionDto(user.getId(),user.getFullName(), user.getPhoneNo(), user.getEmail(), user.getRegistrationDate());
        return userOrderDto;
    }


    public static UserDto mapUserDto(UserEntity user){
        UserDto userDto = new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getPhoneNo(), user.getStatus());
        return userDto;
    }





    public static CreditCard mapCard(SaveCardRequest saveCardRequest){
        CreditCard creditCard = CreditCard.builder()
                .cardNumber(saveCardRequest.getCard_number())
                .expiryMonth(saveCardRequest.getExpiry_month())
                .expiryYear(saveCardRequest.getExpiry_year())
                .cvv(saveCardRequest.getCvv())
                .build();
        return creditCard;
    }


    public static RiderEntity mapRider(RiderRegRequest regRequest){
        RiderEntity rider = new RiderEntity();

            rider.setRole(Role.ROLE_RIDER);
            rider.setStatusActive(ActiveStatus.NOT_ACTIVE);
            rider.setRegistrationDate(System.currentTimeMillis());
            rider.setStatus(Status.RIDER_UNVERIFIED);
            rider.setFullName(regRequest.getFullName());
            rider.setPhoneNo(regRequest.getPhoneNum());
            rider.setLocation(regRequest.getLocation());
            rider.setVehicle(regRequest.getVehicleType());

        return rider;
    }

    public static AdminEntity mapAdmin(AdminRegistration adminRegistration){
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setFullName(adminRegistration.getFullName());
        adminEntity.setEmail(adminRegistration.getEmail());
        adminEntity.setStatus(Status.VERIFIED);
        adminEntity.setActiveStatus(ActiveStatus.NOT_ACTIVE);
        adminEntity.setRole(Role.ROLE_ADMIN);

        return adminEntity;
    }


    public static AdminDto mapAdminDto(AdminEntity adminEntity){
        AdminDto adminDto = new AdminDto();
        adminDto.setId(adminEntity.getId());
        adminDto.setFullName(adminEntity.getFullName());
        adminDto.setEmail(adminEntity.getEmail());
        adminDto.setStatus(adminEntity.getStatus());
        adminDto.setActiveStatus(adminEntity.getActiveStatus());
        adminDto.setRole(adminEntity.getRole());
        adminDto.setRegistrationDate(adminEntity.getRegistrationDate());


        return adminDto;
    }


    public static UserDetails mapUserDetails(UserEntity user){
        UserDetails userDetails = UserDetails.builder()
                .fullName(user.getFullName())
                .registrationDate(user.getRegistrationDate())
                .email(user.getEmail())
                .imagePath(user.getImagePath())
                .phoneNum(user.getPhoneNo())
                .build();

        Wallet wallet = user.getWallet();
        if(wallet != null){
            userDetails.setBalance(wallet.getBalance());
        }else {
            userDetails.setBalance(BigDecimal.valueOf(0.00));
        }

        return userDetails;
    }

    static Boolean isToday(long timeStamp){
        Date theOrderDate = new Date(timeStamp);
        Date todayDate = new Date();
        Calendar orderCalender = Calendar.getInstance();
        Calendar todayCalender = Calendar.getInstance();

        orderCalender.setTime(theOrderDate);
        todayCalender.setTime(todayDate);

        Boolean isToday = orderCalender.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR) &&
                orderCalender.get(Calendar.MONTH) == todayCalender.get(Calendar.MONTH) &&
                orderCalender.get(Calendar.DAY_OF_MONTH) == todayCalender.get(Calendar.DAY_OF_MONTH);
        return isToday;
    }

    static Boolean isThisMonth(long timeStamp){
        Date theOrderDate = new Date(timeStamp);
        Date todayDate = new Date();
        Calendar orderCalender = Calendar.getInstance();
        Calendar todayCalender = Calendar.getInstance();

        orderCalender.setTime(theOrderDate);
        todayCalender.setTime(todayDate);

        Boolean isLastMonth = orderCalender.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR) &&
                orderCalender.get(Calendar.MONTH) == todayCalender.get(Calendar.MONTH);
        return isLastMonth;
        }

    static Boolean isLastMonth(long timeStamp) {
        Date theOrderDate = new Date(timeStamp);
        Date todayDate = new Date();
        Calendar orderCalendar = Calendar.getInstance();
        Calendar todayCalendar = Calendar.getInstance();

        orderCalendar.setTime(theOrderDate);
        todayCalendar.setTime(todayDate);

        int orderYear = orderCalendar.get(Calendar.YEAR);
        int orderMonth = orderCalendar.get(Calendar.MONTH);
        int todayYear = todayCalendar.get(Calendar.YEAR);
        int todayMonth = todayCalendar.get(Calendar.MONTH);

        if (orderYear == todayYear && orderMonth == todayMonth - 1) {
            return true; // Order is from last month
        } else if (orderYear == todayYear - 1 && orderMonth == Calendar.DECEMBER && todayMonth == Calendar.JANUARY) {
            return true; // Handle December of the previous year and January of the current year
        }

        return false; // Order is not from last month
    }

    public static RiderDto mapRiderDto(RiderEntity rider){
        RiderDto riderDto = RiderDto.builder()
                .id(rider.getId())
                .fullName( rider.getFullName())
                .phoneNum(rider.getPhoneNo())
                .balance(rider.getWallet().getBalance())
                .imagePath(rider.getImagePath())
                .location(rider.getLocation())
                .activeStatus(rider.getStatusActive())
                .status(rider.getStatus())
                .pendingJobs((int) rider.getOrders().stream().filter(order ->  order.getOrderStatus() == OrderStatus.PENDING).count())
                .pickedUpJobs((int) rider.getOrders().stream().filter(order -> order.getOrderStatus() == OrderStatus.PROCESSING).count())
                .deliveredJobs((int) rider.getOrders().stream().filter(order ->  order.getOrderStatus() == OrderStatus.DONE && isToday(order.getOrderDate())).count())
                .build();

        return riderDto;
    }

    public static RiderDetails mapRiderDetails(RiderEntity rider){
        AtomicInteger currentMonthRevenue = new AtomicInteger();
        AtomicInteger lastMonthRevenue = new AtomicInteger();

        rider.getOrders().stream().forEachOrdered(order -> {
            if(isThisMonth(order.getOrderDate())){
                currentMonthRevenue.addAndGet((order.getCharge() - (order.getCharge() * 20 / 100)));
            }else if(isLastMonth(order.getOrderDate())){
                lastMonthRevenue.addAndGet((order.getCharge() - (order.getCharge() * 20 / 100)));
            }
        });

        RiderDetails riderDetails = RiderDetails.builder()
                .fullName(rider.getFullName())
                .phoneNum(rider.getPhoneNo())
                .registrationDate(rider.getRegistrationDate())
                .imagePath(rider.getImagePath())
                .location(rider.getLocation())
                .completedOrders((int) rider.getOrders().stream().filter(order -> order.getOrderStatus() == OrderStatus.DONE).count())
                .cancelledOrders((int) rider.getOrders().stream().filter(order -> order.getOrderStatus() == OrderStatus.CANCELLED).count())
                .currentJobs((int) rider.getOrders().stream().filter( order -> order.getOrderStatus() == OrderStatus.PENDING && order.getOrderStatus() == OrderStatus.PROCESSING ).count())
                .vehicle(rider.getVehicle())
                .lastMonthRevenue(lastMonthRevenue.intValue())
                .currentMonthRevenue(currentMonthRevenue.intValue())
                .statusActive(rider.getStatusActive())
                .orderDtoList(rider.getOrders().stream().map(order -> mapOrderDto(order)).collect(Collectors.toList()))



                .build();
        return riderDetails;
    }

    public static TransactionDto mapTransactionDto(Transaction transaction){
        TransactionDto transactionDto = new TransactionDto(transaction.getId(), transaction.getSource(),
                transaction.getDescription(),transaction.getType(),
                transaction.getAmount(),transaction.getDate(),transaction.getStatus(),
                transaction.getFlutterWaveId(),mapUserTransactionDto(transaction.getUser()));
        return transactionDto;
    }




}
