package com.example.countryapiservice.models.dto;

import lombok.Data;

@Data
public class AuthCheckResponseDto {
    private boolean success;
    private String message;
}
