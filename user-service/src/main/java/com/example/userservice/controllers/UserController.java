package com.example.userservice.controllers;

import com.example.userservice.models.UserInfo;
import com.example.userservice.models.dto.AuthenticationResponse;
import com.example.userservice.models.dto.RegisterRequestDto;
import com.example.userservice.models.dto.MessageResponse;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.services.AuthenticationService;
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
}

