package com.eager.questioncloud.authentication.service;

import com.eager.questioncloud.authentication.domain.AuthenticationToken;
import com.eager.questioncloud.authentication.dto.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.authentication.implement.AuthenticationTokenProcessor;
import com.eager.questioncloud.social.implement.SocialAuthenticateProcessor;
import com.eager.questioncloud.user.domain.AccountType;
import com.eager.questioncloud.user.domain.User;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final AuthenticationProcessor authenticationProcessor;
    private final SocialAuthenticateProcessor socialAuthenticateProcessor;

    public AuthenticationToken login(String email, String password) {
        User user = authenticationProcessor.getUserByCredentials(email, password);
        return new AuthenticationToken(
            authenticationTokenProcessor.generateAccessToken(user.getUid()),
            authenticationTokenProcessor.generateRefreshToken(user.getUid()));
    }

    public SocialAuthenticateResponse socialLogin(AccountType accountType, String code) {
        return socialAuthenticateProcessor.socialLogin(accountType, code);
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }

    public void authentication(String token) {
        try {
            Claims claims = authenticationTokenProcessor.getAccessTokenClaimsWithValidate(token);
            Long uid = claims.get("uid", Long.class);
            authenticationProcessor.authentication(uid);
        } catch (Exception ignored) {
        }
    }

    public void guestAuthentication() {
        authenticationProcessor.setGuest();
    }
}