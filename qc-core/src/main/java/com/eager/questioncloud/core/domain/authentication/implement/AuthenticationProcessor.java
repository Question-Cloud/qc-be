package com.eager.questioncloud.core.domain.authentication.implement;

import com.eager.questioncloud.core.domain.authentication.dto.SocialAuthenticateResult;
import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.core.domain.user.implement.CreateSocialUserInformationAppender;
import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final SocialAuthenticateProcessor socialAuthenticateProcessor;
    private final CreateSocialUserInformationAppender createSocialUserInformationAppender;
    private final UserReader userReader;

    public AuthenticationToken emailPasswordAuthentication(String email, String password) {
        User user = getUserWithValidateCredentials(email, password);
        return AuthenticationToken.create(
            authenticationTokenProcessor.generateAccessToken(user.getUid()),
            authenticationTokenProcessor.generateRefreshToken(user.getUid())
        );
    }

    private User getUserWithValidateCredentials(String email, String password) {
        User user = userReader.getUserByEmail(email);
        user.validatePassword(password);
        user.checkUserStatus();
        return user;
    }

    public SocialAuthenticateResult socialAuthentication(String code, AccountType accountType) {
        String socialAccessToken = socialAuthenticateProcessor.getAccessToken(accountType, code);
        String socialUid = socialAuthenticateProcessor.getSocialUid(accountType, socialAccessToken);
        Optional<User> socialUser = userReader.getSocialUser(accountType, socialUid);

        if (socialUser.isPresent()) {
            Long uid = socialUser.get().getUid();
            return SocialAuthenticateResult.success(
                AuthenticationToken.create(
                    authenticationTokenProcessor.generateAccessToken(uid),
                    authenticationTokenProcessor.generateRefreshToken(uid)));
        }

        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationAppender.append(
            CreateSocialUserInformation.create(accountType, socialUid));
        return SocialAuthenticateResult.notRegister(createSocialUserInformation.getRegisterToken());
    }

}
