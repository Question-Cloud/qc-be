package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationCreator {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public CreateSocialUserInformation create(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationRepository.append(createSocialUserInformation);
    }
}
