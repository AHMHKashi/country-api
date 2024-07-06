package com.example.userservice.controllers;


import com.example.userservice.models.UserInfo;
import com.example.userservice.models.dto.MessageResponse;
import com.example.userservice.models.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminController {
    private final UserRepository userRepository;

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
    public ArrayList<UserDto> getUsers() {
        ArrayList<UserDto> userList = new ArrayList<>();
        for (UserInfo userInfo : userRepository.findAll()) {
            userList.add(
              UserDto.builder()
                      .id(userInfo.getId())
                      .username(userInfo.getUsername())
                      .password(userInfo.getPassword())
                      .role(userInfo.getRole())
                      .isActive(userInfo.isActive())
                      .build()
            );
        }
        return (userList);
    }

    @GetMapping("/auth")
    public MessageResponse getAuth() {
        return new MessageResponse("You are admin!");
    }
}
