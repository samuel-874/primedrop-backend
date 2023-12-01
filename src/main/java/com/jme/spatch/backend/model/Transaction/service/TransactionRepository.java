package com.jme.spatch.backend.model.Transaction.service;

import com.jme.spatch.backend.model.Transaction.entity.Transaction;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUser(UserEntity user);
}
