package com.example.countryapi.repository;

import com.example.countryapi.models.UserInfo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<UserInfo, Integer> {

    Optional<UserInfo> findByUsername(String username);
}
