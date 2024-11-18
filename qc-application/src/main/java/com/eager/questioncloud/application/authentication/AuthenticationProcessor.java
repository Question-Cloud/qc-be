package com.eager.questioncloud.application.authentication;

import com.eager.questioncloud.domain.social.SocialAPIManager;
import com.eager.questioncloud.domain.social.SocialPlatform;
import com.eager.questioncloud.domain.user.AccountType;
import com.eager.questioncloud.domain.user.User;
import com.eager.questioncloud.domain.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final SocialAPIManager socialAPIManager;
    private final UserRepository userRepository;

    public AuthenticationToken emailPasswordAuthentication(String email, String password) {
        User user = getUserWithValidateCredentials(email, password);
        return AuthenticationToken.create(
            authenticationTokenProcessor.generateAccessToken(user.getUid()),
            authenticationTokenProcessor.generateRefreshToken(user.getUid())
        );
    }

    private User getUserWithValidateCredentials(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        user.validatePassword(password);
        user.checkUserStatus();
        return user;
    }

    public SocialAuthenticationResult socialAuthentication(String code, AccountType accountType) {
        String socialAccessToken = socialAPIManager.getAccessToken(code, SocialPlatform.from(accountType));
        String socialUid = socialAPIManager.getSocialUid(socialAccessToken, SocialPlatform.from(accountType));
        Optional<User> socialUser = userRepository.getSocialUser(accountType, socialUid);

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
