package com.example.countryapi.repository;

import com.example.countryapi.models.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TokenRepository extends CrudRepository<Token, Integer> {
    Optional<Token> findByName(String name);

    Optional<Token> findByToken(String token);

    @Query("SELECT token FROM Token token WHERE token.userInfo.id = :userInfoId")
    List<Token> findTokensByUserInfo(@Param("userInfoId") Integer userInfoId);

    @Query("SELECT token FROM Token token WHERE token.userInfo.id = :userInfoId AND token.name = :tokenName")
    List<Token> findTokensByUserInfoAndName(@Param("userInfoId") Integer userInfoId, @Param("tokenName") String tokenName);


}
