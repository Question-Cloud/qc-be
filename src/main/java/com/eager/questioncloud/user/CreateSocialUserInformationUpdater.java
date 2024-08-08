package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationUpdater {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public CreateSocialUserInformation use(CreateSocialUserInformation createSocialUserInformation) {
        createSocialUserInformation.use();
        return createSocialUserInformationRepository.save(createSocialUserInformation);
    }
}
