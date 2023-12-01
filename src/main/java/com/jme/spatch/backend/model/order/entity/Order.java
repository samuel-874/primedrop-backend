package com.jme.spatch.backend.model.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @SequenceGenerator(
            name = "order_seq",
            sequenceName = "order_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_seq")
    private long id;
    @Enumerated(EnumType.STRING)
    private PackageType requestType;
    @Enumerated(EnumType.STRING)
    private Vehicle vehicle;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "id")
    @JsonIgnore
    private UserEntity user;
    private String pickOffLocation;
    private String alphaNumericId;
    private String dropOffLocation;
    private int distance;
    private String averageDuration;
    private String receiversName;
    private String sendersName;
    private String receiverPhoneNum;
    private String senderPhoneNum;
    private String itemToDeliver;
    private int itemQuantity;
    private String note;
    private LocalTime pickUpTime ;
    private LocalTime dropOffTime;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "rider_id")
    private RiderEntity rider;
    private long orderDate;
    private int cashToReceive;
    private int charge;
}
