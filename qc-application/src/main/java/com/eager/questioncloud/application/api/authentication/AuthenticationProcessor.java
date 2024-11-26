package com.eager.questioncloud.application.api.authentication;

import com.eager.questioncloud.core.domain.social.SocialAPIManager;
import com.eager.questioncloud.core.domain.social.SocialPlatform;
import com.eager.questioncloud.core.domain.user.AccountType;
import com.eager.questioncloud.core.domain.user.User;
import com.eager.questioncloud.core.domain.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {
    private final SocialAPIManager socialAPIManager;
    private final UserRepository userRepository;

    public User emailPasswordAuthentication(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        user.validatePassword(password);
        user.checkUserStatus();
        return user;
    }

    public SocialAuthentication socialAuthentication(String code, AccountType accountType) {
        String socialAccessToken = socialAPIManager.getAccessToken(code, SocialPlatform.from(accountType));
        String socialUid = socialAPIManager.getSocialUid(socialAccessToken, SocialPlatform.from(accountType));
        Optional<User> optionalUser = userRepository.getSocialUser(accountType, socialUid);
        return optionalUser
            .map(user -> {
                user.checkUserStatus();
                return SocialAuthentication.create(user, socialAccessToken);
            })
            .orElseGet(() -> SocialAuthentication.create(null, socialAccessToken));
    }

}
