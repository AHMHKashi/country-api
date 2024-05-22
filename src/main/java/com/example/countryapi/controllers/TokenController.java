package com.example.countryapi.controllers;


import com.example.countryapi.models.Token;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.models.dto.DeleteTokenResponse;
import com.example.countryapi.models.dto.TokenResponseArray;
import com.example.countryapi.models.dto.TokenResponse;
import com.example.countryapi.repository.TokenRepository;
import com.example.countryapi.repository.UserRepository;
import com.example.countryapi.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class TokenController {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @PostMapping("/api-tokens")
    public TokenResponse createToken(@RequestParam String name, @RequestParam String expire_date) {
        Date date;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            date = formatter.parse(expire_date);
        } catch (Exception e) {
            return null;
        }
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String jwtToken = jwtService.generateToken(userInfo, date);
        Token token = Token.builder().name(name).token(jwtToken).expireDate(date).userInfo(userInfo).build();
        tokenRepository.save(token);
        return TokenResponse.builder().name(name).token(jwtToken).expireDate(date).build();
    }

    @DeleteMapping("/api-tokens")
    public DeleteTokenResponse deleteToken() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes != null) {
            String tokenString = servletRequestAttributes.getRequest().getHeader("Authorization");
            Optional<Token> token = tokenRepository.findByToken(tokenString);
            if (token.isPresent()) {
                tokenRepository.delete(token.get());
                return DeleteTokenResponse.builder().deleted(true).build();
            }
        }
        return DeleteTokenResponse.builder().deleted(false).build();
    }

    @GetMapping("api-tokens")
    public TokenResponseArray getTokens() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Token> tokens = tokenRepository.findTokensByUserInfo(userInfo.getId());
        List<TokenResponse> tokenResponses= new ArrayList<>();
        for(Token token : tokens) {
            TokenResponse tokenResponse = TokenResponse.builder()
                    .name(token.getName())
                    .expireDate(token.getExpireDate())
                    .token(token.getToken())
                    .build();
            tokenResponses.add(tokenResponse);
        }
        return TokenResponseArray.builder()
                .tokens(tokenResponses)
                .count(tokens.size())
                .build();
    }
}
