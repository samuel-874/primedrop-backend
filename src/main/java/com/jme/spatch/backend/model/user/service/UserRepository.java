package com.jme.spatch.backend.model.user.service;

import com.jme.spatch.backend.model.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
    UserEntity findByPhoneNo(String mobile);
    UserEntity findByEmailOrPhoneNo(String usernameOrPhoneNumber, String usernameOrPhoneNumber1);
}
