package com.jme.spatch.backend.model.Transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="transactions")
public class Transaction {
    @Id
    @SequenceGenerator(
            name = "transaction_seq",
            sequenceName = "transaction_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "transaction_seq")
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
    @ManyToOne
    @JsonIgnore
    private UserEntity user;
}
