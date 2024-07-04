package com.example.countryapiservice.configuration;

import com.example.countryapiservice.models.dto.AuthCheckResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CentralAuthFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;

    @Autowired
    public CentralAuthFilter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(401, "Unauthorized");
            return;
        }

        final String jwt = authHeader.substring(7);
        var url = "http://localhost:8080/users/check-token?token=" + jwt;

        try {
            var result = restTemplate.getForObject(url, AuthCheckResponseDto.class);
            System.out.println("result: " + result);
            if (result == null) {
                response.sendError(401, "Unknown Issue");
            } else if (!result.isSuccess()) {
                response.sendError(401, result.getMessage());
            } else {
                filterChain.doFilter(request, response);
            }
        }catch (Exception e){
            response.sendError(401, "Unauthorized");
        }
    }
}
