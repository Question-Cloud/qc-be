package com.eager.questioncloud.core.domain.social;

import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoAPI extends SocialAPI {
    @Value("${KAKAO_API_KEY}")
    private String KAKAO_API_KEY;

    @Value("${KAKAO_API_SECRET}")
    private String KAKAO_API_SECRET;

    @Value("${CLIENT_URL}")
    private String CLIENT_URL;

    @Override
    public String getAccessToken(String code) {
        WebClient webClient = WebClient.create("https://kauth.kakao.com/oauth/token");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "authorization_code");
        formData.add("client_id", KAKAO_API_KEY);
        formData.add("redirect_uri", CLIENT_URL + "/user/social/kakao");
        formData.add("code", code);
        formData.add("client_secret", KAKAO_API_SECRET);

        SocialAccessToken res = webClient.post()
            .headers(headers -> headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .exchangeToMono(response -> response.bodyToMono(SocialAccessToken.class))
            .block();

        if (res == null || res.access_token() == null) {
            throw new CoreException(Error.FAIL_SOCIAL_LOGIN);
        }

        return res.access_token();
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken) {
        WebClient webClient = WebClient.create("https://kapi.kakao.com/v1/oidc/userinfo");
        KakaoUserInfo kakaoUserInfo = webClient.get()
            .headers(header -> {
                header.add("Authorization", "Bearer " + accessToken);
            })
            .exchangeToMono(response -> response.bodyToMono(KakaoUserInfo.class))
            .block();

        if (kakaoUserInfo == null || kakaoUserInfo.sub() == null) {
            throw new CoreException(Error.FAIL_SOCIAL_LOGIN);
        }

        return new SocialUserInfo(kakaoUserInfo.sub(), kakaoUserInfo.email(), kakaoUserInfo.nickname(), AccountType.KAKAO);
    }

    @Override
    SocialPlatform getSocialPlatform() {
        return SocialPlatform.KAKAO;
    }

    record KakaoUserInfo(String sub, String email, String nickname) {
    }
}
