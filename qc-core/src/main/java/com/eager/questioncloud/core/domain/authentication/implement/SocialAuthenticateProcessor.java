package com.eager.questioncloud.core.domain.authentication.implement;

import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAuthenticateProcessor {
    private final KakaoAPI kakaoProcessor;
    private final GoogleAPI googleProcessor;
    private final NaverAPI naverProcessor;

    public String getAccessToken(AccountType accountType, String code) {
        switch (accountType) {
            case KAKAO -> {
                return kakaoProcessor.getAccessToken(code);
            }
            case GOOGLE -> {
                return googleProcessor.getAccessToken(code);
            }
            case NAVER -> {
                return naverProcessor.getAccessToken(code);
            }
            default -> {
                throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
            }
        }
    }

    public String getSocialUid(AccountType accountType, String accessToken) {
        switch (accountType) {
            case KAKAO -> {
                return kakaoProcessor.getUserInfo(accessToken).uid();
            }
            case GOOGLE -> {
                return googleProcessor.getUserInfo(accessToken).uid();
            }
            case NAVER -> {
                return naverProcessor.getUserInfo(accessToken).uid();
            }
            default -> {
                throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
            }
        }
    }
}
