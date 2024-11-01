package com.eager.questioncloud.core.domain.authentication.service;

import com.eager.questioncloud.core.domain.authentication.dto.SocialAuthenticateResult;
import com.eager.questioncloud.core.domain.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.core.domain.authentication.implement.AuthenticationTokenProcessor;
import com.eager.questioncloud.core.domain.authentication.implement.SocialAuthenticateProcessor;
import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.core.domain.user.implement.CreateSocialUserInformationAppender;
import com.eager.questioncloud.core.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final AuthenticationProcessor authenticationProcessor;
    private final SocialAuthenticateProcessor socialAuthenticateProcessor;
    private final CreateSocialUserInformationAppender createSocialUserInformationAppender;

    public AuthenticationToken login(String email, String password) {
        return authenticationProcessor.emailPasswordAuthentication(email, password);
    }

    public SocialAuthenticateResult socialLogin(AccountType accountType, String code) {
        String socialUid = socialAuthenticateProcessor.socialAuthentication(accountType, code);
        Optional<User> socialUser = authenticationProcessor.getUserBySocialUid(accountType, socialUid);
        if (socialUser.isPresent()) {
            socialUser.get().checkUserStatus();
            return SocialAuthenticateResult.success(
                new AuthenticationToken(
                    authenticationTokenProcessor.generateAccessToken(socialUser.get().getUid()),
                    authenticationTokenProcessor.generateRefreshToken(socialUser.get().getUid())));
        }
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationAppender.append(
            CreateSocialUserInformation.create(accountType, socialUid));
        return SocialAuthenticateResult.notRegister(createSocialUserInformation.getRegisterToken());
    }

    public AuthenticationToken refresh(String refreshToken) {
        return authenticationTokenProcessor.refresh(refreshToken);
    }
}
