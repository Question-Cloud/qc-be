package com.eager.questioncloud.domain.social.implement;

import com.eager.questioncloud.domain.authentication.dto.Response.SocialAuthenticateResponse;
import com.eager.questioncloud.domain.authentication.implement.AuthenticationProcessor;
import com.eager.questioncloud.domain.authentication.implement.AuthenticationTokenProcessor;
import com.eager.questioncloud.domain.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.domain.user.implement.CreateSocialUserInformationAppender;
import com.eager.questioncloud.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.vo.AccountType;
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
    private final CreateSocialUserInformationAppender createSocialUserInformationAppender;

    public String getSocialUid(AccountType accountType, String code) {
        switch (accountType) {
            case KAKAO -> {
                String accessToken = kakaoProcessor.getAccessToken(code);
                return kakaoProcessor.getUserInfo(accessToken).uid();
            }
            case GOOGLE -> {
                String accessToken = googleProcessor.getAccessToken(code);
                return googleProcessor.getUserInfo(accessToken).uid();
            }
            case NAVER -> {
                String accessToken = naverProcessor.getAccessToken(code);
                return naverProcessor.getUserInfo(accessToken).uid();
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
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationAppender.append(
            CreateSocialUserInformation.create(accountType, socialUid));
        return SocialAuthenticateResponse.needsRegister(createSocialUserInformation.getRegisterToken());
    }
}
