package com.eager.questioncloud.domain.authentication.dto;

import com.eager.questioncloud.domain.authentication.vo.AuthenticationToken;
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

        public static SocialAuthenticateResponse login(AuthenticationToken authenticationToken) {
            return new SocialAuthenticateResponse(true, null, authenticationToken);
        }

        public static SocialAuthenticateResponse needsRegister(String registerToken) {
            return new SocialAuthenticateResponse(false, registerToken, null);
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
