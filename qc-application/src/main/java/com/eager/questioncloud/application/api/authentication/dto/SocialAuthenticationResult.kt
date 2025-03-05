package com.eager.questioncloud.application.api.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialAuthenticationResult {
    private Boolean isRegistered;
    private String registerToken;
    private AuthenticationToken authenticationToken;

    public static SocialAuthenticationResult success(AuthenticationToken authenticationToken) {
        return new SocialAuthenticationResult(true, null, authenticationToken);
    }

    public static SocialAuthenticationResult notRegister(String registerToken) {
        return new SocialAuthenticationResult(false, registerToken, null);
    }
}
