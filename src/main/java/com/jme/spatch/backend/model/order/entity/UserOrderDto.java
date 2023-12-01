package com.jme.spatch.backend.model.order.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderDto {
    private long id;
    private String name;
    private String phoneNum;
    private String email;


    public UserOrderDto(String name, String phoneNum, String email) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
    }
}
