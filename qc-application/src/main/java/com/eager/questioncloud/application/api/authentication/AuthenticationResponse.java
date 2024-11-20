package com.eager.questioncloud.application.api.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AuthenticationResponse {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SocialAuthenticateResponse {
        private Boolean isRegistered;
        private String registerToken;
        private AuthenticationToken authenticationToken;

        public static SocialAuthenticateResponse create(SocialAuthenticationResult socialAuthenticationResult) {
            return new SocialAuthenticateResponse(
                socialAuthenticationResult.getIsRegistered(),
                socialAuthenticationResult.getRegisterToken(),
                socialAuthenticationResult.getAuthenticationToken());

        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LoginResponse {
        private AuthenticationToken authenticationToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RefreshResponse {
        private AuthenticationToken authenticationToken;
    }
}
