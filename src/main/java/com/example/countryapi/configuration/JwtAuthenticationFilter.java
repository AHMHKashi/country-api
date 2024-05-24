package com.example.countryapi.configuration;

import com.example.countryapi.models.Role;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.services.JwtService;
import com.example.countryapi.services.UserInfoDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserInfoDetailService userInfoDetailService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(401, "Unauthorized");
            return;
        }
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            response.sendError(401, "Unauthorized");
            return;
        }

        UserInfo userInfo = this.userInfoDetailService.loadUserByUsername(username);
        if (!userInfo.getRole().equals(Role.ADMIN) && !userInfo.isActive()) {
            response.sendError(403, "User is not active");
            return;
        }

        if (!jwtService.isTokenValid(jwt, userInfo)) {
            response.sendError(401, "Invalid token");
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request)
    {
        String path = request.getRequestURI();
        return path.startsWith("/users/");
    }
}
