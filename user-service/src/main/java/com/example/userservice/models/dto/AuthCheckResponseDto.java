package com.example.userservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCheckResponseDto {
    private boolean success;
    private String message;
}
