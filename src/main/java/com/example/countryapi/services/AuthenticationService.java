package com.example.countryapi.services;

import com.example.countryapi.models.Role;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.models.dto.AuthenticationResponse;
import com.example.countryapi.models.dto.RegisterRequestDto;
import com.example.countryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequestDto registerForm) {
        var user = UserInfo.builder()
                .username(registerForm.getUsername())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(RegisterRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getUsername()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        return null;
    }
}
