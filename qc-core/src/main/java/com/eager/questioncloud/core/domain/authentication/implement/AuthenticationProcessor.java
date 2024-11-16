package com.eager.questioncloud.core.domain.authentication.implement;

import com.eager.questioncloud.core.domain.authentication.dto.AuthenticationToken;
import com.eager.questioncloud.core.domain.authentication.dto.SocialAuthenticationResult;
import com.eager.questioncloud.core.domain.social.SocialAPIManager;
import com.eager.questioncloud.core.domain.social.SocialPlatform;
import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final SocialAPIManager socialAPIManager;
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

    public SocialAuthenticationResult socialAuthentication(String code, AccountType accountType) {
        String socialAccessToken = socialAPIManager.getAccessToken(code, SocialPlatform.from(accountType));
        String socialUid = socialAPIManager.getSocialUid(socialAccessToken, SocialPlatform.from(accountType));
        Optional<User> socialUser = userReader.getSocialUser(accountType, socialUid);

        if (socialUser.isPresent()) {
            Long uid = socialUser.get().getUid();
            return SocialAuthenticationResult.success(
                AuthenticationToken.create(
                    authenticationTokenProcessor.generateAccessToken(uid),
                    authenticationTokenProcessor.generateRefreshToken(uid)));
        }
        return SocialAuthenticationResult.notRegister(socialAccessToken);
    }

}
