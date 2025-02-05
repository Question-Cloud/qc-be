package com.eager.questioncloud.application.business.authentication.service;

import com.eager.questioncloud.application.api.authentication.dto.AuthenticationToken;
import com.eager.questioncloud.application.api.authentication.dto.SocialAuthentication;
import com.eager.questioncloud.application.api.authentication.dto.SocialAuthenticationResult;
import com.eager.questioncloud.application.business.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.application.business.authentication.implement.AuthenticationTokenManager;
import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.model.User;
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
