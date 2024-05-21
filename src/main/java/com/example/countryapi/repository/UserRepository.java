package com.example.countryapi.repository;

import com.example.countryapi.models.UserInfo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<UserInfo, Integer> {

    Optional<UserInfo> findByUsername(String username);
}
