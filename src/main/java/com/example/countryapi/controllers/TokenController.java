package com.example.countryapi.controllers;



import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
@RestController
@RequestMapping("/user")
public class TokenController {

    @PostMapping("/api-tokens")
    public String createToken (@RequestParam String name, @RequestParam LocalDateTime dateTime) {
        return null;
    }

    @DeleteMapping("/api-tokens")
    public String deleteToken() {
        return "DONE";
    }
}
