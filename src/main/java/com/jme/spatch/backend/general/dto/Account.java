package com.jme.spatch.backend.general.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String fullName;
    private String email;

    private String phoneNo;
    private BigDecimal balance;

    private String imagePath;

}
