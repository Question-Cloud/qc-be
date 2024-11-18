package com.eager.questioncloud.domain.social;

import com.eager.questioncloud.domain.user.vo.AccountType;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleAPI extends SocialAPI {
    @Value("${GOOGLE_CLIENT_ID}")
    private String GOOGLE_CLIENT_ID;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${CLIENT_URL}")
    private String CLIENT_URL;

    @Override
    public String getAccessToken(String code) {
        WebClient webClient = WebClient.create("https://oauth2.googleapis.com/token");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("client_id", GOOGLE_CLIENT_ID);
        formData.add("client_secret", GOOGLE_CLIENT_SECRET);
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", CLIENT_URL + "/user/social/google");

        SocialAccessToken res = webClient.post()
            .headers(headers -> headers.add("Content-Type",
                "application/x-www-form-urlencoded;charset=utf-8"))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .exchangeToMono(response -> response.bodyToMono(SocialAccessToken.class))
            .block();

        if (res == null || res.access_token() == null) {
            throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
        }

        return res.access_token();
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken) {
        WebClient webClient = WebClient.create("https://www.googleapis.com/oauth2/v1/userinfo?alt=json");

        GoogleUserInfo googleUserInfo = webClient.get()
            .headers(header -> {
                header.add("Authorization", "Bearer " + accessToken);
            })
            .exchangeToMono(response -> response.bodyToMono(GoogleUserInfo.class))
            .block();

        if (googleUserInfo == null || googleUserInfo.id() == null) {
            throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
        }

        return new SocialUserInfo(googleUserInfo.id(), googleUserInfo.email(), googleUserInfo.name(), AccountType.GOOGLE);
    }

    @Override
    SocialPlatform getSocialPlatform() {
        return SocialPlatform.GOOGLE;
    }

    record GoogleUserInfo(String id, String email, String name, String picture) {
    }
}
