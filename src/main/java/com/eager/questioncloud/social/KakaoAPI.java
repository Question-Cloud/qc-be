package com.eager.questioncloud.social;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.AccountType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoAPI implements SocialAPI {
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

        if (res == null || res.getAccess_token() == null) {
            throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
        }

        return res.getAccess_token();
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken) {
        WebClient webClient = WebClient.create("https://kapi.kakao.com/v1/oidc/userinfo");
        System.out.println(accessToken);
        KakaoUserInfo kakaoUserInfo = webClient.get()
            .headers(header -> {
                header.add("Authorization", "Bearer " + accessToken);
            })
            .exchangeToMono(response -> response.bodyToMono(KakaoUserInfo.class))
            .block();

        if (kakaoUserInfo == null || kakaoUserInfo.getSub() == null) {
            throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
        }

        return new SocialUserInfo(kakaoUserInfo.getSub(), kakaoUserInfo.getEmail(), kakaoUserInfo.getNickname(), AccountType.KAKAO);
    }
}