package com.jme.spatch.backend.general.dto;

import com.jme.spatch.backend.model.Transaction.entity.TransactionStatus;
import com.jme.spatch.backend.model.Transaction.entity.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
        private long id;
        private String source;
        private String description;
        @Enumerated(EnumType.STRING)
        private TransactionType type;
        private BigDecimal amount;
        private LocalDateTime date;
        @Enumerated(EnumType.STRING)
        private TransactionStatus status;
        private long flutterWaveId;
        private UserTransactionDto user;


}
