package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationUpdater {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public void use(CreateSocialUserInformation createSocialUserInformation) {
        createSocialUserInformation.use();
        createSocialUserInformationRepository.save(createSocialUserInformation);
    }
}
