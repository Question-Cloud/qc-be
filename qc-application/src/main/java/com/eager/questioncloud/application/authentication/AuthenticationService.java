package com.eager.questioncloud.application.authentication;

import com.eager.questioncloud.domain.user.AccountType;
import com.eager.questioncloud.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationProcessor authenticationProcessor;
    private final AuthenticationTokenProcessor authenticationTokenProcessor;

    public AuthenticationToken login(String email, String password) {
        User user = authenticationProcessor.emailPasswordAuthentication(email, password);
        String accessToken = authenticationTokenProcessor.generateAccessToken(user.getUid());
        String refreshToken = authenticationTokenProcessor.generateRefreshToken(user.getUid());
        return AuthenticationToken.create(accessToken, refreshToken);
    }

    public SocialAuthenticationResult socialLogin(AccountType accountType, String code) {
        SocialAuthentication socialAuthentication = authenticationProcessor.socialAuthentication(code, accountType);
        if (socialAuthentication.isRegistered()) {
            String accessToken = authenticationTokenProcessor.generateAccessToken(socialAuthentication.getUser().getUid());
            String refreshToken = authenticationTokenProcessor.generateRefreshToken(socialAuthentication.getUser().getUid());
            return SocialAuthenticationResult.success(AuthenticationToken.create(accessToken, refreshToken));
        }
        return SocialAuthenticationResult.notRegister(socialAuthentication.getSocialAccessToken());
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }
}
