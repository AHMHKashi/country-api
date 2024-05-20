package com.example.countryapi.controllers;


import com.example.countryapi.models.UserInfo;
import com.example.countryapi.utility.JwtUtil;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
@RestController
@RequestMapping("/user")
public class TokenController {

    @PostMapping("/api-tokens")
    public String createToken (@RequestParam String name, @RequestParam LocalDateTime dateTime) {
        String token = JwtUtil.generateToken(name);
        return token;
    }

    @DeleteMapping("/api-tokens")
    public String deleteToken() {
        return "DONE";
    }
}
