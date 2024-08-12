package com.eager.questioncloud.social;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAuthenticateProcessor {
    private final KakaoAPI kakaoProcessor;

    public String getSocialUid(AccountType accountType, String code) {
        switch (accountType) {
            case KAKAO -> {
                String accessToken = kakaoProcessor.getAccessToken(code);
                return kakaoProcessor.getUserInfo(accessToken).getUid();
            }
            default -> {
                throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
            }
        }
    }
}
