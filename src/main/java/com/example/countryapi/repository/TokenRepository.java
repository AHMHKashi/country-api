package com.example.countryapi.repository;

import com.example.countryapi.models.Token;
import com.example.countryapi.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;



public interface TokenRepository extends CrudRepository<Token, Integer> {
    Optional<Token> findByName(String name);
    Optional<Token> findByToken(String token);
    @Query( "SELECT token FROM UserInfo userInfo join userInfo.tokens token WHERE userInfo.id = :userInfoId")
    List<Token> findTokensByUserInfo(@Param("userInfoId") Integer userInfoId);


}
