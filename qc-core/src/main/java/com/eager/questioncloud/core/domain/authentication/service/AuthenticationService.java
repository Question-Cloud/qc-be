package com.eager.questioncloud.core.domain.authentication.service;

import com.eager.questioncloud.core.domain.authentication.dto.SocialAuthenticateResult;
import com.eager.questioncloud.core.domain.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.core.domain.authentication.implement.AuthenticationTokenProcessor;
import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
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

    public SocialAuthenticateResult socialLogin(AccountType accountType, String code) {
        return authenticationProcessor.socialAuthentication(code, accountType);
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }
}
