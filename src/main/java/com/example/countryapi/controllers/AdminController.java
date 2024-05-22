package com.example.countryapi.controllers;


import com.example.countryapi.models.UserInfo;
import com.example.countryapi.models.dto.MessageResponse;
import com.example.countryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminController {
    private final UserRepository userRepository;
    //PUT /admin/users?username={username}&active={active(true/false)

    @PutMapping("")
    public MessageResponse setActiveUser(@RequestParam String username, @RequestParam boolean active) {
        Optional<UserInfo> userInfo = userRepository.findByUsername(username);
        if (userInfo.isEmpty()) {
            return new MessageResponse("couldn't find a user with given username");
        }
        userInfo.get().setActive(active);
        userRepository.save(userInfo.get());
        return new MessageResponse("activeness of user with username " + username + " is now " + active);
    }

    @GetMapping("")
    public Iterable<UserInfo> getUsers() {
        return (userRepository.findAll());
    }
}
