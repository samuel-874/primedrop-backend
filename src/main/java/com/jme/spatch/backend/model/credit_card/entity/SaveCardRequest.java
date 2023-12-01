package com.jme.spatch.backend.model.credit_card.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveCardRequest {
    private String card_number;
    private String expiry_month;
    private String expiry_year;
    private String cvv;
};