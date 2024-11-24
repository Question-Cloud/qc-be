package com.eager.questioncloud.application.api.authentication;

import com.eager.questioncloud.core.domain.user.User;
import lombok.Getter;

@Getter
public class SocialAuthentication {
    private final User user;
    private final String socialAccessToken;

    private SocialAuthentication(User user, String socialAccessToken) {
        this.user = user;
        this.socialAccessToken = socialAccessToken;
    }

    public static SocialAuthentication create(User user, String socialAccessToken) {
        return new SocialAuthentication(user, socialAccessToken);
    }

    public Boolean isRegistered() {
        return this.user != null;
    }
}
