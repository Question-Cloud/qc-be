package com.eager.questioncloud.api.authentication;

import com.eager.questioncloud.core.domain.authentication.dto.AuthenticationToken;
import com.eager.questioncloud.core.domain.authentication.dto.SocialAuthenticationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Response {
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
