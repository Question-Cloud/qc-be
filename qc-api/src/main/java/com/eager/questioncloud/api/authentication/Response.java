package com.eager.questioncloud.api.authentication;

import com.eager.questioncloud.core.domain.authentication.dto.SocialAuthenticateResult;
import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
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

        public static SocialAuthenticateResponse create(SocialAuthenticateResult socialAuthenticateResult) {
            return new SocialAuthenticateResponse(
                socialAuthenticateResult.getIsRegistered(),
                socialAuthenticateResult.getRegisterToken(),
                socialAuthenticateResult.getAuthenticationToken());

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
