package com.example.countryapi.controllers;

import com.example.countryapi.models.RegisterRequestDto;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.repository.UserRepository;
import com.example.countryapi.services.JwtService;
import com.example.countryapi.services.UserService;
import com.example.countryapi.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private UserService userService;

    private JwtService jwtService;
    @Autowired

    public UserController(UserService userService, JwtService jwtService, UserRepository userRepository) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<HttpStatus> registerUser (@RequestBody RegisterRequestDto registerForm) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        UserInfo user = new UserInfo();
        user.setUsername(registerForm.getUsername());
        user.setPassword(registerForm.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @GetMapping(path="/all")
//    public @ResponseBody Iterable<User> getAllUsers() {
//        // This returns a JSON or XML with the users
//        return userRepository.findAll();
//    }



    @PostMapping("/login")
    public String loginUser(@RequestBody UserInfo userProfile) {
        // if username and password is valid:
        String token = JwtUtil.generateToken(userProfile.getUsername());
        return token;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<UserInfo> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}

