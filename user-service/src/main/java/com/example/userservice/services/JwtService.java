package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "RamZc+WGeGNHA4SD5HyQBWFix/V/u18LWXNt+IkShduFpaKoy3kOUsRiqwz+NU+ngLtQlgirLg8jmlfzFXVMObqYzHKtalqJspuUBPgTYrnE0djbXgz42GOAvNhVyBMnYJ3qkkjTL6ronG6j+063aHQktEVjQ/yeRPqFmzI3A8XXgZiaj5Zcs6tdax6MT1PSx3N3Hd/N+maLqfsAch7HiIPUrruHPOpkMLczOHPl+xfhqrUuk9SR58mA3is3cnhWfyBTv52yIRUpYLaC19huQIXdldascLAbjVaevn6HNSrf0dXypiaoFUM9/inKM9KwZmYD3eHuY4HWfnxscYwnEfwoVzH8LRJdJBMH/7pF/CE=";
    private final TokenRepository tokenRepository;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(
            UserDetails userDetails,
            Date expireDate
    ) {
        return generateToken(new HashMap<>(), userDetails, expireDate);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            Date expireDate

    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expireDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String tokenString, UserDetails userDetails) {
        final String username = extractUsername(tokenString);
        Optional<Token> token = tokenRepository.findByToken(tokenString);
        if (token.isPresent()) {
            if (isTokenExpired(tokenString)) {
                tokenRepository.delete(token.get());
                return false;
            }
            return username.equals(userDetails.getUsername());
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
