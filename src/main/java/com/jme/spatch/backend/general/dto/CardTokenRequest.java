package com.jme.spatch.backend.general.dto;

import lombok.Data;

@Data
public class CardTokenRequest {
    private String card_number;
    private String cvv;
    private String expiry_month;
    private String expiry_year;
}
