package com.eager.questioncloud.user;

import com.eager.questioncloud.social.KakaoProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialAuthenticateService {
    private final KakaoProcessor kakaoProcessor;

    public String getSocialUid(AccountType accountType, String code) {
        switch (accountType) {
            case KAKAO -> {
                String accessToken = kakaoProcessor.getAccessToken(code);
                return kakaoProcessor.getUserInfo(accessToken).getUid();
            }
            default -> {
                throw new RuntimeException();
            }
        }
    }
}
