package com.eager.questioncloud.core.domain.social;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAPIManager {
    private final List<SocialAPI> socialAPIs;

    public String getAccessToken(String code, SocialPlatform socialPlatform) {
        for (SocialAPI socialAPI : socialAPIs) {
            if (socialAPI.getSocialPlatform().equals(socialPlatform)) {
                return socialAPI.getAccessToken(code);
            }
        }
        throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
    }

    public String getSocialUid(String accessToken, SocialPlatform socialPlatform) {
        for (SocialAPI socialAPI : socialAPIs) {
            if (socialAPI.getSocialPlatform().equals(socialPlatform)) {
                return socialAPI.getUserInfo(accessToken).uid();
            }
        }
        throw new CustomException(Error.FAIL_SOCIAL_LOGIN);
    }
}
