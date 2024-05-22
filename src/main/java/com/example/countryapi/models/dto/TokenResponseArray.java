package com.example.countryapi.models.dto;


import com.example.countryapi.models.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseArray {
    List<TokenResponse> tokens;
    int count;

}
