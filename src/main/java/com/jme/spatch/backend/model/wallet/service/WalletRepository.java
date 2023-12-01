package com.jme.spatch.backend.model.wallet.service;

import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findByUser(UserEntity user);
}
