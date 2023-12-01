package com.jme.spatch.backend.model.credit_card.service;

import com.jme.spatch.backend.model.credit_card.entity.CreditCard;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard,Long> {

    List<CreditCard> findByCardHolder(UserEntity user);
}
