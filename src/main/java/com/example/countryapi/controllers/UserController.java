package com.example.countryapi.controllers;

import com.example.countryapi.models.dto.AuthenticationResponse;
import com.example.countryapi.models.dto.RegisterRequestDto;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.repository.UserRepository;
import com.example.countryapi.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final AuthenticationService authService;



    @PostMapping("/register")
    public @ResponseBody ResponseEntity<AuthenticationResponse> registerUser (@RequestBody RegisterRequestDto registerForm) {
        return ResponseEntity.ok(authService.register(registerForm));
    }



    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody RegisterRequestDto registerForm) {
        return ResponseEntity.ok(authService.authenticate(registerForm));
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<UserInfo> getAllUsers() {
        return userRepository.findAll();
    }
}

