package com.eager.questioncloud.application.api.authentication;

import lombok.Getter;

@Getter
public class AuthenticationToken {
    private String accessToken;
    private String refreshToken;

    private AuthenticationToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static AuthenticationToken create(String accessToken, String refreshToken) {
        return new AuthenticationToken(accessToken, refreshToken);
    }
}
