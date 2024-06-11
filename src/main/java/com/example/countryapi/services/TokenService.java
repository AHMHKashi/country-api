package com.example.countryapi.services;

import com.example.countryapi.models.Token;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.models.dto.DeleteTokenResponse;
import com.example.countryapi.models.dto.TokenResponse;
import com.example.countryapi.models.dto.TokenResponseArray;
import com.example.countryapi.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    final TokenRepository tokenRepository;
    final JwtService jwtService;
    public TokenResponse createToken(String tokenName, String tokenExpireDate) {
        if (tokenName.equals("temp-token")) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This name is reserved.");
        }
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Token> searchToken = tokenRepository.findTokensByUserInfoAndName(userInfo.getId(), tokenName);
        if (!searchToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "There is already a token with this name.");
        }
        Date date;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            date = formatter.parse(tokenExpireDate);
        } catch (Exception e) {
            return null;
        }
        String jwtToken = jwtService.generateToken(userInfo, date);
        Token token = Token.builder().name(tokenName).token(jwtToken).expireDate(date).userInfo(userInfo).build();
        tokenRepository.save(token);
        return TokenResponse.builder().name(tokenName).token(jwtToken).expireDate(date).build();
    }

    public DeleteTokenResponse deleteToken() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes != null) {
            String tokenString = servletRequestAttributes.getRequest().getHeader("Authorization");
            Optional<Token> token = tokenRepository.findByToken(tokenString.substring(7));
            if (token.isPresent() && !token.get().getName().equals("temp-token")) {
                tokenRepository.delete(token.get());
                return DeleteTokenResponse.builder().deleted(true).build();
            }
        }
        return DeleteTokenResponse.builder().deleted(false).build();
    }

    public TokenResponseArray getTokens() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Token> tokens = tokenRepository.findTokensByUserInfo(userInfo.getId());
        List<TokenResponse> tokenResponses = new ArrayList<>();
        for (Token token : tokens) {
            TokenResponse tokenResponse = TokenResponse.builder()
                    .name(token.getName())
                    .expireDate(token.getExpireDate())
                    .token("API ***")
                    .build();
            tokenResponses.add(tokenResponse);
        }
        return TokenResponseArray.builder()
                .tokens(tokenResponses)
                .count(tokens.size())
                .build();
    }


}
