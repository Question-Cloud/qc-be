package com.eager.questioncloud.user.dto;

import com.eager.questioncloud.authentication.AuthenticationToken;
import com.eager.questioncloud.user.dto.UserDto.MyInformation;
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

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CreateUserResponse {
        private String resendToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class MyInformationResponse {
        private MyInformation myInformation;
    }
}
