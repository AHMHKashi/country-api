package com.example.countryapi.services;

import com.example.countryapi.models.Role;
import com.example.countryapi.models.Token;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.models.dto.AuthenticationResponse;
import com.example.countryapi.models.dto.RegisterRequestDto;
import com.example.countryapi.repository.TokenRepository;
import com.example.countryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public UserInfo register(RegisterRequestDto registerForm) {
        var user = UserInfo.builder()
                .username(registerForm.getUsername())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        return user;
    }

    public AuthenticationResponse authenticate(RegisterRequestDto request) {
        Optional<UserInfo> searchUser = repository.findByUsername(request.getUsername());
        if (searchUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "There is no user with this username.");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your password is incorrect.");
        }

        UserInfo user = searchUser.get();
        if (!user.isActive() && !user.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active.");
        }
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        var jwtToken = jwtService.generateToken(user, date);
        List<Token> searchToken = tokenRepository.findTokensByUserInfoAndName(user.getId(), "temp-token");
        Token token;
        if (!searchToken.isEmpty()) {
            token = Token.builder()
                    .userInfo(user)
                    .token(jwtToken)
                    .expireDate(date)
                    .name("temp-token")
                    .id(searchToken.get(0).getId())
                    .build();
        } else {
            token = Token.builder()
                    .userInfo(user)
                    .token(jwtToken)
                    .expireDate(date)
                    .name("temp-token")
                    .build();
        }
        tokenRepository.save(token);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
