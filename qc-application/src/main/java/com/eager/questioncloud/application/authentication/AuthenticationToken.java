package com.eager.questioncloud.application.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationToken {
    private String accessToken;
    private String refreshToken;

    public static AuthenticationToken create(String accessToken, String refreshToken) {
        return new AuthenticationToken(accessToken, refreshToken);
    }
}
