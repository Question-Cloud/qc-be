package com.eager.questioncloud.user;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final AuthenticationManager authenticationManager;

    public User authentication(String loginId, String password) {
        return authenticationManager.authentication(loginId, password);
    }

    public AuthenticationToken generateAuthenticateToken(Long uid) {
        return new AuthenticationToken(authenticationTokenProcessor.generateAccessToken(uid), authenticationTokenProcessor.generateRefreshToken(uid));
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }

    public void tokenAuthentication(String token) {
        try {
            Claims claims = authenticationTokenProcessor.getAccessTokenClaimsWithValidate(token);
            Long uid = claims.get("uid", Long.class);
            authenticationManager.springSecurityAuthentication(uid);
        } catch (Exception ignored) {
        }
    }
}
