package com.jme.spatch.backend.model.rider.service;

import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RiderRepository extends JpaRepository<RiderEntity,Long> {
    RiderEntity findByPhoneNo(String origin);

    List<RiderEntity> findByStatusActive(ActiveStatus active);

    RiderEntity findByEmailOrPhoneNo(String email, String phoneNo);
}
