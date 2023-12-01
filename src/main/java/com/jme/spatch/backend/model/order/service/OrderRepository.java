package com.jme.spatch.backend.model.order.service;

import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(UserEntity user);

    Optional<Order> findByAlphaNumericId(String builder);
    List<Order> findByRider(RiderEntity rider);
}
