package com.eager.questioncloud.social;

import com.eager.questioncloud.authentication.AuthenticationManager;
import com.eager.questioncloud.authentication.AuthenticationToken;
import com.eager.questioncloud.authentication.AuthenticationTokenProcessor;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.AccountType;
import com.eager.questioncloud.user.CreateSocialUserInformation;
import com.eager.questioncloud.user.CreateSocialUserInformationProcessor;
import com.eager.questioncloud.user.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.user.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAuthenticateProcessor {
    private final KakaoAPI kakaoProcessor;
    private final GoogleAPI googleProcessor;
    private final AuthenticationManager authenticationManager;
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
            default -> {
                throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
            }
        }
    }

    public SocialAuthenticateResponse socialLogin(AccountType accountType, String code) {
        String socialUid = getSocialUid(accountType, code);
        Optional<User> socialUser = authenticationManager.getUserBySocialUid(accountType, socialUid);
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
