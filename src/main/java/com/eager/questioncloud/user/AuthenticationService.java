package com.eager.questioncloud.user;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final AuthenticationManager authenticationManager;

    public AuthenticationToken login(String loginId, String password) {
        User user = authenticationManager.getUserByCredentials(loginId, password);
        return new AuthenticationToken(
            authenticationTokenProcessor.generateAccessToken(user.getUid()),
            authenticationTokenProcessor.generateRefreshToken(user.getUid()));
    }

    public AuthenticationToken generateAuthenticateToken(Long uid) {
        return new AuthenticationToken(authenticationTokenProcessor.generateAccessToken(uid), authenticationTokenProcessor.generateRefreshToken(uid));
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }

    public void authentication(String token) {
        try {
            Claims claims = authenticationTokenProcessor.getAccessTokenClaimsWithValidate(token);
            Long uid = claims.get("uid", Long.class);
            authenticationManager.authentication(uid);
        } catch (Exception ignored) {
        }
    }
}
