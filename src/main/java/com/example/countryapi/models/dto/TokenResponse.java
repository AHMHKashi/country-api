package com.example.countryapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String name;
    private Date expireDate;
    private String token;

//    {
//        "name": "my-token-1",
//            "expire_date": "2025-01-01T23:59:59Z",
//            "token": "API ierkse325dfgawmd9hbwppp"
//    }
}
