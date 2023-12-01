package com.jme.spatch.backend.model.wallet.entity;

import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @SequenceGenerator(
            name = "wallet_seq",
            sequenceName = "wallet_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "wallet_seq")
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity user;
    private BigDecimal balance;
}
