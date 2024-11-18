package com.eager.questioncloud.application.authentication;

import com.eager.questioncloud.domain.user.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationProcessor authenticationProcessor;
    private final AuthenticationTokenProcessor authenticationTokenProcessor;

    public AuthenticationToken login(String email, String password) {
        return authenticationProcessor.emailPasswordAuthentication(email, password);
    }

    public SocialAuthenticationResult socialLogin(AccountType accountType, String code) {
        return authenticationProcessor.socialAuthentication(code, accountType);
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }
}
