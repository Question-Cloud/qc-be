package com.eager.questioncloud.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationToken {
    private String accessToken;
    private String refreshToken;
}
