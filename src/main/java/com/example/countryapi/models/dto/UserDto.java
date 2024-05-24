package com.example.countryapi.models.dto;

import com.example.countryapi.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String username;
    private String password;
    private boolean isActive;
    private Role role;

}
