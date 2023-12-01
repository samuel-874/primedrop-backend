package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.order.entity.PackageType;
import com.jme.spatch.backend.model.order.entity.UserOrderRole;
import com.jme.spatch.backend.model.order.entity.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "Package  is required")
    private PackageType requestType = PackageType.SAME_DAY;
    @NotNull(message = "Vehicle's name  is required")
    private Vehicle vehicle = Vehicle.BIKE;
    @NotNull(message = "The server needs to know if user is the sender or the receiver")
    private UserOrderRole usersIs = UserOrderRole.SENDER;
    @NotBlank(message = "Pickup location is required is required")
    private String pickOffLocation;
    @NotBlank(message = "Drop off location is required")
    private String dropOffLocation;
    @NotNull(message = "Distance is required")
    private int distance;
    @NotBlank(message = "Duration is required")
    private String averageDuration;
    @NotBlank(message = "receiver's name is required")
    private String receiversName;
    @NotBlank(message = "sender's name is required")
    private String sendersName;
    @NotBlank(message = "sender's no. is required")
    private String senderPhoneNum;
    @NotBlank(message = "receiver's no. is required")
    private String receiverPhoneNum;
    @NotBlank(message = "Item name is required is required")
    private String itemToDeliver;
    @NotNull(message = "Quantity of the item ro be delivered  is required")
    private int itemQuantity;
    @NotBlank(message = "A short note is required")
    private String note;
    @NotNull( message = "Field is required")
    private int cashToReceive;
    @NotNull(message = "Charge is required")
    private int charge;
}