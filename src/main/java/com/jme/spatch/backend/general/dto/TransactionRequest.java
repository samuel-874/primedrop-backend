package com.jme.spatch.backend.general.dto;


import com.jme.spatch.backend.model.Transaction.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NotNull(message = "Source is required")
    private String source;
    @NotNull(message = "Amount is required")
    private int amount;
    @NotNull(message = "TransactionType is required")
    private TransactionType type;
    private String description;
}
