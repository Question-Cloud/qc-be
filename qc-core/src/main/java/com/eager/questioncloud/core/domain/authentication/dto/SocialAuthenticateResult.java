package com.eager.questioncloud.core.domain.authentication.dto;

import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialAuthenticateResult {
    private Boolean isRegistered;
    private String registerToken;
    private AuthenticationToken authenticationToken;

    public static SocialAuthenticateResult success(AuthenticationToken authenticationToken) {
        return new SocialAuthenticateResult(true, null, authenticationToken);
    }

    public static SocialAuthenticateResult notRegister(String registerToken) {
        return new SocialAuthenticateResult(false, registerToken, null);
    }
}
