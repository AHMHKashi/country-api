package com.example.countryapi.models;


import lombok.Data;

@Data
public class UserInfo {

    private String username;
    private String password;
    private boolean isActive;


}
