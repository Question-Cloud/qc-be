package com.eager.questioncloud.core.domain.authentication.service;

import com.eager.questioncloud.core.domain.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.core.domain.authentication.implement.AuthenticationTokenProcessor;
import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.core.domain.social.dto.SocialAuthenticateResult;
import com.eager.questioncloud.core.domain.social.implement.SocialAuthenticateProcessor;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
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
        String accessToken = authenticationTokenProcessor.generateAccessToken(user.getUid());
        String refreshToken = authenticationTokenProcessor.generateRefreshToken(user.getUid());
        return AuthenticationToken.create(accessToken, refreshToken);
    }

    public SocialAuthenticateResult socialLogin(AccountType accountType, String code) {
        return socialAuthenticateProcessor.socialLogin(accountType, code);
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }
}
