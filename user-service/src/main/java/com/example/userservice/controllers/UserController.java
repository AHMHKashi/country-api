package com.example.userservice.controllers;

import com.example.userservice.models.Role;
import com.example.userservice.models.UserInfo;
import com.example.userservice.models.dto.AuthCheckResponseDto;
import com.example.userservice.models.dto.AuthenticationResponse;
import com.example.userservice.models.dto.RegisterRequestDto;
import com.example.userservice.models.dto.MessageResponse;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.services.AuthenticationService;
import com.example.userservice.services.JwtService;
import com.example.userservice.services.UserInfoDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final AuthenticationService authService;
    private final JwtService jwtService;
    private final UserInfoDetailService userInfoDetailService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterRequestDto registerForm) {
        if (userRepository.findByUsername(registerForm.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "There is already a user with this name");
        }
        UserInfo user = authService.register(registerForm);
        return ResponseEntity.ok(new MessageResponse("User created successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody RegisterRequestDto registerForm) {
        return ResponseEntity.ok(authService.authenticate(registerForm));
    }

    @GetMapping("/check-token")
    public ResponseEntity<AuthCheckResponseDto> checkToken(@RequestParam String token) {
        String username;

        username = jwtService.extractUsername(token);
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        UserInfo userInfo = userInfoDetailService.loadUserByUsername(username);
        if (!userInfo.getRole().equals(Role.ADMIN) && !userInfo.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active");
        }
        if (!jwtService.isTokenValid(token, userInfo)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        var authCheckResponseDto = new AuthCheckResponseDto(true, "success");
        return ResponseEntity.ok(authCheckResponseDto);
    }
}

