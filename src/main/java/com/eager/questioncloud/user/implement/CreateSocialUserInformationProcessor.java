package com.eager.questioncloud.user.implement;

import com.eager.questioncloud.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.user.vo.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationProcessor {
    private final CreateSocialUserInformationAppender createSocialUserInformationAppender;
    private final CreateSocialUserInformationReader createSocialUserInformationReader;
    private final CreateSocialUserInformationUpdater createSocialUserInformationUpdater;

    public CreateSocialUserInformation create(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationAppender.append(createSocialUserInformation);
    }

    public CreateSocialUserInformation use(String token, AccountType accountType) {
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationReader.find(token, accountType);
        createSocialUserInformationUpdater.use(createSocialUserInformation);
        return createSocialUserInformation;
    }
}
