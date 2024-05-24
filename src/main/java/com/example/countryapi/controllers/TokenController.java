package com.example.countryapi.controllers;


import com.example.countryapi.models.Token;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.models.dto.*;
import com.example.countryapi.repository.TokenRepository;
import com.example.countryapi.repository.UserRepository;
import com.example.countryapi.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/api-tokens")
@RequiredArgsConstructor
public class TokenController {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;



    @PostMapping("")
    public TokenResponse createToken(@RequestBody CreateTokenRequestDto request) {
        if (request.getName().equals("temp-token")) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This name is reserved.");
        }
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Token> searchToken = tokenRepository.findTokensByUserInfoAndName(userInfo.getId(), request.getName());
        if (!searchToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "There is already a token with this name.");
        }
        Date date;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            date = formatter.parse(request.getExpire_date());
        } catch (Exception e) {
            return null;
        }
        String jwtToken = jwtService.generateToken(userInfo, date);
        Token token = Token.builder().name(request.getName()).token(jwtToken).expireDate(date).userInfo(userInfo).build();
        tokenRepository.save(token);
        return TokenResponse.builder().name(request.getName()).token(jwtToken).expireDate(date).build();
    }

    @DeleteMapping("")
    public DeleteTokenResponse deleteToken() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes != null) {
            String tokenString = servletRequestAttributes.getRequest().getHeader("Authorization");
            Optional<Token> token = tokenRepository.findByToken(tokenString.substring(7));
            System.out.println(tokenString);
            if (token.isPresent()) {
                for (Token to: tokenRepository.findAll()) System.out.println(to.getName() + " || " + to.getId());
                tokenRepository.deleteById(token.get().getId());
                for (Token to: tokenRepository.findAll()) System.out.println(to);
                return DeleteTokenResponse.builder().deleted(true).build();
            } else System.out.println("TOKEN COULDN'T BE FOUND");
        } else System.out.println("SERVLET IS NULL!!");
        return DeleteTokenResponse.builder().deleted(false).build();
    }

    @GetMapping("")
    public TokenResponseArray getTokens() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Token> tokens = tokenRepository.findTokensByUserInfo(userInfo.getId());
        List<TokenResponse> tokenResponses = new ArrayList<>();
        for (Token token : tokens) {
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
