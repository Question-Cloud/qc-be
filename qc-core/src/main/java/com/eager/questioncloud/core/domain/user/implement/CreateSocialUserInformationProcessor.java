package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationProcessor {
    private final CreateSocialUserInformationReader createSocialUserInformationReader;
    private final CreateSocialUserInformationUpdater createSocialUserInformationUpdater;

    public CreateSocialUserInformation use(String token, AccountType accountType) {
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationReader.find(token, accountType);
        createSocialUserInformationUpdater.use(createSocialUserInformation);
        return createSocialUserInformation;
    }
}
