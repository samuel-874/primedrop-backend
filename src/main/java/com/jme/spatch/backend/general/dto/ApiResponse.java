package com.jme.spatch.backend.general.dto;

import lombok.Data;

@Data
public class ApiResponse {
    private Data data;


    @lombok.Data
    public static class Data {
        private String card_token;
    }
}