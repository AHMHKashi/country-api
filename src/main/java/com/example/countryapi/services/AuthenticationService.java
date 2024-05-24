package com.example.countryapi.services;

import com.example.countryapi.models.Role;
import com.example.countryapi.models.Token;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.models.dto.AuthenticationResponse;
import com.example.countryapi.models.dto.RegisterRequestDto;
import com.example.countryapi.models.dto.MessageResponse;
import com.example.countryapi.repository.TokenRepository;
import com.example.countryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public MessageResponse register(RegisterRequestDto registerForm) {
        var user = UserInfo.builder()
                .username(registerForm.getUsername())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        return MessageResponse.builder()
                .message("User created successfully!")
                .build();
    }

    public AuthenticationResponse authenticate(RegisterRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        var jwtToken = jwtService.generateToken(user, date);
        Optional<Token> searchToken = tokenRepository.findByToken(request.getUsername());
        searchToken.ifPresent(tokenRepository::delete);
        Token token = Token.builder()
                .userInfo(user)
                .token(jwtToken)
                .expireDate(date)
                .name("temp-token")
                .build();
        tokenRepository.save(token);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
