package com.jme.spatch.backend.model.credit_card.entity;

import com.jme.spatch.backend.model.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    @Id
    @SequenceGenerator(
            name = "credit_card_seq",
            sequenceName = "credit_card_seq",
            allocationSize = 1
    )
    @GeneratedValue( strategy = GenerationType.SEQUENCE ,generator = "credit_card_seq")
    private long id;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    @ManyToOne
    private UserEntity cardHolder;
    private String cardToken;
    private String cvv;
}
