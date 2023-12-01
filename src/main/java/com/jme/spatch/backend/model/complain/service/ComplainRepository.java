package com.jme.spatch.backend.model.complain.service;

import com.jme.spatch.backend.model.complain.entity.Complain;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainRepository extends JpaRepository<Complain,Long> {
    List <Complain> findByUser(UserEntity user);
}
