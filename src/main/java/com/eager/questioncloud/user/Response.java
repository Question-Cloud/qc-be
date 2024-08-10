package com.eager.questioncloud.user;

import com.eager.questioncloud.authentication.AuthenticationToken;
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
