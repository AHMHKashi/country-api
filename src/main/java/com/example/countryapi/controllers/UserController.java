package com.example.countryapi.controllers;

import com.example.countryapi.models.UserInfo;
import com.example.countryapi.services.JwtService;
import com.example.countryapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    private JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    public void registerUser() {

    }

    @PostMapping("/login")
    public void loginUser(@RequestBody UserInfo userProfile) {

    }
}
