package com.eager.questioncloud.social.implement;

import com.eager.questioncloud.authentication.domain.AuthenticationToken;
import com.eager.questioncloud.authentication.dto.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.authentication.implement.AuthenticationTokenProcessor;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.domain.AccountType;
import com.eager.questioncloud.user.domain.CreateSocialUserInformation;
import com.eager.questioncloud.user.domain.User;
import com.eager.questioncloud.user.implement.CreateSocialUserInformationProcessor;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAuthenticateProcessor {
    private final KakaoAPI kakaoProcessor;
    private final GoogleAPI googleProcessor;
    private final NaverAPI naverProcessor;
    private final AuthenticationProcessor authenticationProcessor;
    private final AuthenticationTokenProcessor authenticationTokenProcessor;
    private final CreateSocialUserInformationProcessor createSocialUserInformationProcessor;

    public String getSocialUid(AccountType accountType, String code) {
        switch (accountType) {
            case KAKAO -> {
                String accessToken = kakaoProcessor.getAccessToken(code);
                return kakaoProcessor.getUserInfo(accessToken).getUid();
            }
            case GOOGLE -> {
                String accessToken = googleProcessor.getAccessToken(code);
                return googleProcessor.getUserInfo(accessToken).getUid();
            }
            case NAVER -> {
                String accessToken = naverProcessor.getAccessToken(code);
                return naverProcessor.getUserInfo(accessToken).getUid();
            }
            default -> {
                throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
            }
        }
    }

    public SocialAuthenticateResponse socialLogin(AccountType accountType, String code) {
        String socialUid = getSocialUid(accountType, code);
        Optional<User> socialUser = authenticationProcessor.getUserBySocialUid(accountType, socialUid);
        if (socialUser.isPresent()) {
            socialUser.get().checkUserStatus();
            return SocialAuthenticateResponse.login(new AuthenticationToken(
                authenticationTokenProcessor.generateAccessToken(socialUser.get().getUid()),
                authenticationTokenProcessor.generateRefreshToken(socialUser.get().getUid())));
        }
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationProcessor.create(
            CreateSocialUserInformation.create(accountType, socialUid));
        return SocialAuthenticateResponse.needsRegister(createSocialUserInformation.getRegisterToken());
    }
}
