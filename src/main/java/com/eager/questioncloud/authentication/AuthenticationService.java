package com.eager.questioncloud.authentication;

import com.eager.questioncloud.social.SocialAuthenticateProcessor;
import com.eager.questioncloud.user.AccountType;
import com.eager.questioncloud.user.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.user.User;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final AuthenticationManager authenticationManager;
    private final SocialAuthenticateProcessor socialAuthenticateProcessor;

    public AuthenticationToken login(String email, String password) {
        User user = authenticationManager.getUserByCredentials(email, password);
        return new AuthenticationToken(
            authenticationTokenProcessor.generateAccessToken(user.getUid()),
            authenticationTokenProcessor.generateRefreshToken(user.getUid()));
    }

    public SocialAuthenticateResponse socialLogin(AccountType accountType, String code) {
        return socialAuthenticateProcessor.socialLogin(accountType, code);
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
