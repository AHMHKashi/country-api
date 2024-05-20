package com.example.countryapi.models;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String username;
    private String password;
}
