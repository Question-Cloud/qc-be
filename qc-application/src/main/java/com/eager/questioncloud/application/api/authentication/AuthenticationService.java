package com.eager.questioncloud.application.api.authentication;

import com.eager.questioncloud.domain.user.AccountType;
import com.eager.questioncloud.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationProcessor authenticationProcessor;
    private final AuthenticationTokenManager authenticationTokenManager;

    public AuthenticationToken login(String email, String password) {
        User user = authenticationProcessor.emailPasswordAuthentication(email, password);
        return authenticationTokenManager.create(user.getUid());
    }

    public SocialAuthenticationResult socialLogin(AccountType accountType, String code) {
        SocialAuthentication socialAuthentication = authenticationProcessor.socialAuthentication(code, accountType);
        if (socialAuthentication.isRegistered()) {
            return SocialAuthenticationResult.success(authenticationTokenManager.create(socialAuthentication.getUser().getUid()));
        }
        return SocialAuthenticationResult.notRegister(socialAuthentication.getSocialAccessToken());
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenManager.refresh(refreshToken);
    }
}
