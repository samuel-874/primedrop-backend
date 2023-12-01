package com.jme.spatch.backend.general.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FundRequest {
    private long id;
    private String source;
    private int amount;
    private String description;
}
