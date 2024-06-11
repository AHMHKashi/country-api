package com.example.countryapi.controllers;



import com.example.countryapi.models.dto.*;
import com.example.countryapi.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user/api-tokens")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;


    @PostMapping("")
    public TokenResponse createToken(@RequestBody CreateTokenRequestDto request) {
        return tokenService.createToken(request.getName(), request.getExpire_date());
    }

    @DeleteMapping("")
    public DeleteTokenResponse deleteToken() {
        return tokenService.deleteToken();
    }

    @GetMapping("")
    public TokenResponseArray getTokens() {
        return tokenService.getTokens();
    }
}
