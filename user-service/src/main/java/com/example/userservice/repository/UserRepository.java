package com.example.userservice.repository;

import com.example.userservice.models.UserInfo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends CrudRepository<UserInfo, Integer> {

    Optional<UserInfo> findByUsername(String username);

    @Transactional
    void deleteUserInfoByUsername(String username);
}
