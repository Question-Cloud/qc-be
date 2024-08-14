package com.eager.questioncloud.social;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.AccountType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverAPI implements SocialAPI {
    @Value("${NAVER_CLIENT_ID}")
    private String NAVER_CLIENT_ID;

    @Value("${NAVER_CLIENT_SECRET}")
    private String NAVER_CLIENT_SECRET;

    @Override
    public String getAccessToken(String code) {
        WebClient webClient = WebClient.create("https://nid.naver.com/oauth2.0/token");

        SocialAccessToken res = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", NAVER_CLIENT_ID)
                .queryParam("client_secret", NAVER_CLIENT_SECRET)
                .queryParam("code", code)
                .build())
            .exchangeToMono(response -> response.bodyToMono(SocialAccessToken.class))
            .block();

        if (res == null || res.getAccess_token() == null) {
            throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
        }

        return res.getAccess_token();
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken) {
        WebClient webClient = WebClient.create("https://openapi.naver.com/v1/nid/me");

        NaverUserInfoAPIResponse apiResponse = webClient.get()
            .headers(header -> {
                header.add("Authorization", "Bearer " + accessToken);
            })
            .exchangeToMono(response -> response.bodyToMono(NaverUserInfoAPIResponse.class))
            .block();

        if (apiResponse == null || apiResponse.getResponse() == null || apiResponse.getResponse().getId() == null) {
            throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
        }

        NaverUserInfo naverUserInfo = apiResponse.getResponse();

        return new SocialUserInfo(naverUserInfo.getId(), naverUserInfo.getEmail(), naverUserInfo.getNickname(), AccountType.NAVER);
    }
}
