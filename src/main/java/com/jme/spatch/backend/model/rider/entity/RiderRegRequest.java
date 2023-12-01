package com.jme.spatch.backend.model.rider.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiderRegRequest {
    @NotBlank(message = " Full name required")
    private String fullName;
    @NotBlank(message = " Mobile no. required")
    @Length(max = 14 , min = 14,message = "Invalid Mobile no.")
    private String phoneNum;
    private DeliveryLocation location;
    private VehicleType vehicleType;
    @NotBlank
    @Length(min = 6)
    private String password;
//    private String email;
}
